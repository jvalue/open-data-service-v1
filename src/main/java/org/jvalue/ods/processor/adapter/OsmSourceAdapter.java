/*
    Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.utils.Log;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import crosby.binary.osmosis.OsmosisReader;


final class OsmSourceAdapter extends AbstractSourceAdapter {

	@Inject
	OsmSourceAdapter(@Assisted DataSource source, @Assisted String sourceUrl, MetricRegistry registry) {
		super(source, sourceUrl, registry);
	}


	@Override
	protected SourceIterator doCreateIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		return new OsmosisSourceIterator(source, sourceUrl, registry);
	}


	private static final class OsmosisSourceIterator extends SourceIterator {

		private final BlockingQueue<Optional<ObjectNode>> jsonQueue = new ArrayBlockingQueue<>(100);
		private final JsonSink jsonSink = new JsonSink(jsonQueue);

		private OsmosisReader osmosisReader = null;
		private ObjectNode currentNode = null;

		public OsmosisSourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
			super(source, sourceUrl, registry);
		}


		@Override
		protected boolean doHasNext() {
			try {
				initOsmoisReader();
				if (currentNode == null) {
					Optional<ObjectNode> optional = jsonQueue.take();
					if (optional.isPresent()) currentNode = optional.get();
				}
				return currentNode != null;
			} catch (InterruptedException | IOException e) {
				throw new SourceAdapterException(e);
			}
		}


		@Override
		protected ObjectNode doNext() {
			try {
				initOsmoisReader();
				ObjectNode retValue = currentNode;
				currentNode = null;
				return retValue;
			} catch (IOException e) {
				throw new SourceAdapterException(e);
			}
		}


		private void initOsmoisReader() throws IOException {
			if (osmosisReader == null) {
				osmosisReader = new OsmosisReader(sourceUrl.openStream());
				osmosisReader.setSink(jsonSink);
				Thread thread = new Thread(osmosisReader);
				thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread thread, Throwable throwable) {
						// TODO exception is not carried back to the chain manager
						Log.error("osmosis reader failed", throwable);
					}
				});
				thread.start();
			}
		}
	}


	private static class JsonSink implements Sink {

		private final BlockingQueue<Optional<ObjectNode>> jsonQueue;
		private final ObjectMapper mapper = new ObjectMapper();

		public JsonSink(BlockingQueue<Optional<ObjectNode>> jsonQueue) {
			this.jsonQueue = jsonQueue;
			this.mapper.addMixInAnnotations(Entity.class, EntityMixin.class);
		}


		@Override
		public void process(EntityContainer entityContainer) {
			Entity entity = entityContainer.getEntity();
			ObjectNode jsonObject = mapper.valueToTree(entity);
			try {
				jsonQueue.put(Optional.of(jsonObject));
			} catch (InterruptedException ie) {
				throw new SourceAdapterException(ie);
			}
		}


		@Override
		public void initialize(Map<String, Object> metaData) {  }


		@Override
		public void complete() {
			try {
				jsonQueue.put(Optional.<ObjectNode>absent());
			} catch (InterruptedException ie) {
				throw new SourceAdapterException(ie);
			}
		}


		@Override
		public void release() {
			complete();
		}


		@JsonIgnoreProperties("writeableInstance")
		private static interface EntityMixin { }
	}


}
