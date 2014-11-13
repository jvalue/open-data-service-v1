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
package org.jvalue.ods.filter.grabber;

import java.io.IOException;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.utils.Log;
import org.jvalue.ods.utils.HttpUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class JsonGrabber extends Grabber<JsonNode> {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public JsonGrabber(DataSource source) {
		super(source);
	}


	@Override
	public JsonNode grabSource() {
		JsonNode rootNode = null;
		try {
			String json = HttpUtils.readUrl(dataSource.getUrl(), "UTF-8");
			rootNode = mapper.readTree(json);

		} catch (IOException e) {
			Log.error(e.getMessage());
		}
		
		return rootNode;
	}

}
