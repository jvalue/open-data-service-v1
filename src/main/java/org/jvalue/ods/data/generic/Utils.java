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
package org.jvalue.ods.data.generic;

import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;


public final class Utils {

	private Utils() { }

	public static GenericEntity convertFromJson(JsonNode rootNode) {
		GenericEntity gv = null;

		if (rootNode.isBoolean()) {
			gv = new BaseObject(rootNode.asBoolean());
		} else if (rootNode.isArray()) {
			gv = new ListObject();
			fillListRec(rootNode, (ListObject) gv);
		} else if (rootNode.isObject()) {
			gv = new MapObject();
			fillMapRec(rootNode, (MapObject) gv);
		} else if (rootNode.isNull()) {
			gv = new BaseObject(null);
		} else if (rootNode.isNumber()) {
			gv = new BaseObject(rootNode.numberValue());
		} else if (rootNode.isTextual()) {
			gv = new BaseObject(rootNode.asText());
		}

		return gv;
	}


	private static void fillListRec(JsonNode node, ListObject lv) {
		for (JsonNode n : node) {
			GenericEntity gv = convertFromJson(n);
			lv.getList().add(gv);
		}
	}


	private static void fillMapRec(JsonNode node, MapObject mv) {
		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();
			GenericEntity gv = convertFromJson(field.getValue());
			mv.getMap().put(field.getKey(), gv);
		}
	}
}
