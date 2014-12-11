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
package org.jvalue.ods.filter.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.FilterException;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import java.io.IOException;
import java.util.Map;

import crosby.binary.osmosis.OsmosisReader;


final class OsmSourceAdapter extends SourceAdapter {

	@JsonIgnoreProperties("writeableInstance")
	private static interface EntityMixin { }

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.addMixInAnnotations(Entity.class, EntityMixin.class);
	}

	@Inject
	OsmSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	public ArrayNode grabSource() throws FilterException {
		final ArrayNode jsonArray = new ArrayNode(JsonNodeFactory.instance);
		try {
			OsmosisReader reader = new OsmosisReader(dataSource.getUrl().openStream());
			reader.setSink(new Sink() {
				@Override
				public void process(EntityContainer entityContainer) {
					Entity entity = entityContainer.getEntity();
					jsonArray.add(mapper.valueToTree(entity));
				}

				@Override
				public void initialize(Map<String, Object> metaData) {  }

				@Override
				public void complete() {  }

				@Override
				public void release() {  }
			});
			reader.run();
			return jsonArray;

		} catch (IOException ioe) {
			throw new FilterException(ioe);
		}
	}

}
