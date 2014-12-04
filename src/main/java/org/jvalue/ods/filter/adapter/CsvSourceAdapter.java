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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.FilterException;
import org.jvalue.ods.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;


final class CsvSourceAdapter extends SourceAdapter {

	@Inject
	CsvSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	public ArrayNode grabSource() throws FilterException {
		BufferedReader reader = null;
		try {
			URLConnection connection = dataSource.getUrl().openConnection();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;

			// read header line
			line = reader.readLine();
			String[] keys = line.split(",");

			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				array.add(createObject(keys, values));
			}

			return array;

		} catch (IOException ioe) {
			throw new FilterException(ioe);
		} finally {
			if (reader != null) {
				try { reader.close(); } catch(IOException ioe) { Log.error("failed to close reader", ioe); }
			}
		}
	}


	private ObjectNode createObject(String[] keys, String[] values) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		for (int i = 0; i < keys.length; ++i) {
			node.put(keys[i], values[i]);
		}
		return node;
	}

}
