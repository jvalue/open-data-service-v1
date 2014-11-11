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
package org.jvalue.ods.translator;

import org.jvalue.ods.utils.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class JsonTranslator extends Translator<JsonNode> {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public Object translate(JsonNode jsonNode) {
		try {
			return mapper.treeToValue(jsonNode, Object.class);
		} catch (JsonProcessingException jpe) {
			Log.error(jpe.getMessage());
			return null;
		}
	}

}
