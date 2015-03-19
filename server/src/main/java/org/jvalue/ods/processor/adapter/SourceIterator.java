/*  Open Data Service
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.common.utils.Assert;
import org.jvalue.ods.admin.monitoring.PauseableTimer;
import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;


abstract class SourceIterator implements Iterator<ObjectNode> {

	protected final DataSource source;
	protected final URL sourceUrl;
	private final PauseableTimer.Context context;

	public SourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		Assert.assertNotNull(source, sourceUrl, registry);
		this.source = source;
		this.sourceUrl = sourceUrl;
		this.context = PauseableTimer.createTimer(registry, MetricRegistry.name(this.getClass(), source.getId())).createContext();
	}


	@Override
	public final boolean hasNext() {
		context.resume();
		try {
			boolean hasNext = doHasNext();
			if (hasNext) context.pause();
			else context.stop();
			return hasNext;
		} catch (IOException e) {
			context.stop();
			throw new SourceAdapterException(e);
		}
	}


	@Override
	public final ObjectNode next() {
		if (!hasNext()) throw new NoSuchElementException();
		context.resume();
		try {
			JsonNode nextJsonNode = doNext();

			// wrap none objects
			ObjectNode nextObject;
			if (nextJsonNode.isObject()) {
				nextObject = (ObjectNode) nextJsonNode;
			} else {
				nextObject = new ObjectNode(JsonNodeFactory.instance);
				nextObject.set("value", nextJsonNode);
			}
			context.pause();
			return nextObject;
		} catch (IOException e) {
			context.stop();
			throw new SourceAdapterException(e);
		}
	}


	@Override
	public final void remove() {
		throw new UnsupportedOperationException();
	}


	protected abstract JsonNode doNext() throws IOException;
	protected abstract boolean doHasNext() throws IOException;

}
