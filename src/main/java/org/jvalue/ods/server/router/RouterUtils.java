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
package org.jvalue.ods.server.router;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jvalue.ods.db.DbAccessor;
import org.restlet.Request;
import org.restlet.data.Form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


final class RouterUtils {

	private RouterUtils() { }


	private static final ObjectMapper mapper = new ObjectMapper();

	public static JsonNode getDocumentByAttribute(DbAccessor<JsonNode> dbAccessor, Request request) {
		Form f = request.getResourceRef().getQueryAsForm();
		String key = f.get(0).getName();
		String value = f.get(0).getValue();

		dbAccessor.connect();
		List<JsonNode> nodes = dbAccessor.getAllDocuments();
		List<JsonNode> resultNodes = new LinkedList<JsonNode>();

		for (JsonNode node : nodes) {
			if (!node.isObject()) continue;

			ObjectNode object = (ObjectNode) node;

			Iterator<String> iter = object.fieldNames();
			while (iter.hasNext()) {
				String fieldName = iter.next();
				if (fieldName.equals(key)) {
					JsonNode valueNode = object.get(fieldName);
					if (valueNode.isTextual() && valueNode.asText().equals(value)) {
						resultNodes.add(node);
					}
				}
			}
		}

		return mapper.valueToTree(resultNodes);
	}

}
