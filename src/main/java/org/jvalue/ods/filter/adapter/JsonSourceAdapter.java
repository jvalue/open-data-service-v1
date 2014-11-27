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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.FilterException;

import java.io.IOException;
import java.net.URL;


final class JsonSourceAdapter extends SourceAdapter {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	@Inject
	JsonSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	public ArrayNode grabSource() throws FilterException {
		try {
			JsonNode node = mapper.readTree(new URL(dataSource.getUrl()));
			if (node instanceof ArrayNode) return (ArrayNode) node;
			else {
				ArrayNode arrayNode = mapper.createArrayNode();
				arrayNode.add(node);
				return arrayNode;
			}

		} catch (IOException e) {
			throw new FilterException("failed to parse JSON",  e);
		}
	}

}
