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

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jvalue.ods.data.DataSource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;


final class CsvSourceAdapter extends AbstractSourceAdapter {

	private final CSVFormat csvFormat;

	@Inject
	CsvSourceAdapter(@Assisted DataSource source, @Assisted String csvFormatString) {
		super(source);
		switch (csvFormatString) {
			case "DEFAULT":
				csvFormat = CSVFormat.DEFAULT;
				break;

			case "EXCEL":
				csvFormat = CSVFormat.EXCEL;
				break;

			case "MYSQL":
				csvFormat = CSVFormat.MYSQL;
				break;

			case "RFC4180":
				csvFormat = CSVFormat.RFC4180;
				break;

			case "TDF":
				csvFormat = CSVFormat.TDF;
				break;

			default:
				throw new IllegalArgumentException("unknown csv format \"" + csvFormatString + "\"");
		}
	}


	@Override
	protected SourceIterator doCreateIterator(DataSource source) {
		return new CsvSourceIterator(source, csvFormat);
	}


	private static final class CsvSourceIterator extends SourceIterator {

		private final DataSource source;
		private final CSVFormat csvFormat;
		private Iterator<CSVRecord> csvIterator;
		private CSVRecord firstRecord = null;

		public CsvSourceIterator(DataSource source, CSVFormat csvFormat) {
			this.source = source;
			this.csvFormat = csvFormat;
		}


		@Override
		protected boolean doHasNext() {
			try {
				initCsvIterator();
				return csvIterator.hasNext();
			} catch (IOException ioe) {
				throw new SourceAdapterException(ioe);
			}
		}


		@Override
		protected ObjectNode doNext() {
			try {
				initCsvIterator();
				return createObject(firstRecord, csvIterator.next());
			} catch (IOException ioe) {
				throw new SourceAdapterException(ioe);
			}
		}


		private void initCsvIterator() throws IOException {
			if (csvIterator == null) {
				CSVParser parser = CSVParser.parse(source.getUrl(), Charset.forName("UTF-8"), csvFormat);
				csvIterator = parser.iterator();
				firstRecord = csvIterator.next();
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

}
