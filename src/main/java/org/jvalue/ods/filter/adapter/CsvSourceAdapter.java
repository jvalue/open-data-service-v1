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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.FilterException;
import org.jvalue.ods.utils.Log;

import java.io.IOException;
import java.nio.charset.Charset;


final class CsvSourceAdapter extends SourceAdapter {

	private final CSVFormat csvFormat;

	@Inject
	CsvSourceAdapter(@Assisted DataSource source, @Assisted String csvFormatString) {
		super(source);
		if (csvFormatString.equals(("DEFAULT"))) csvFormat = CSVFormat.DEFAULT;
		else if (csvFormatString.equals(("EXCEL"))) csvFormat = CSVFormat.EXCEL;
		else if (csvFormatString.equals(("MYSQL"))) csvFormat = CSVFormat.MYSQL;
		else if (csvFormatString.equals(("RFC4180"))) csvFormat = CSVFormat.RFC4180;
		else if (csvFormatString.equals(("TDF"))) csvFormat = CSVFormat.TDF;
		else throw new IllegalArgumentException("unknown csv format \"" + csvFormatString + "\"");
	}


	@Override
	public ArrayNode grabSource() throws FilterException {
		CSVParser parser = null;
		try {
			ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
			parser = CSVParser.parse(dataSource.getUrl(), Charset.forName("UTF-8"), csvFormat);

			CSVRecord firstRecord = null;
			for (CSVRecord record : parser) {
				if (firstRecord == null) {
					firstRecord = record;
					continue;
				}
				array.add(createObject(firstRecord, record));
			}

			return array;

		} catch (IOException ioe) {
			throw new FilterException(ioe);
		} finally {
			if (parser != null) try { parser.close(); } catch (IOException ioe) { Log.error("failed to close parser"); }
		}
	}


	private ObjectNode createObject(CSVRecord keys, CSVRecord values) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		for (int i = 0; i < keys.size(); ++i) {
			node.put(keys.get(i), values.get(i));
		}
		return node;
	}

}
