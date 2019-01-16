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

    SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;


final class CsvSourceAdapter extends AbstractSourceAdapter {

	private final CSVFormat csvFormat;

	@Inject
	CsvSourceAdapter(
			@Assisted DataSource source,
			@Assisted(SourceAdapterFactory.ARGUMENT_SOURCE_URL) String sourceUrl,
			@Assisted(SourceAdapterFactory.ARGUMENT_CSV_FORMAT) String csvFormatString,
			MetricRegistry registry) {

		super(source, sourceUrl, registry);
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
	protected SourceIterator doCreateIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		return new CsvSourceIterator(source, sourceUrl, csvFormat, registry);
	}


	private static final class CsvSourceIterator extends SourceIterator {

		private Iterator<CSVRecord> csvIterator;
		private CSVRecord firstRecord;

		public CsvSourceIterator(DataSource source, URL sourceUrl, CSVFormat csvFormat, MetricRegistry registry) throws SourceAdapterException {
			super(source, sourceUrl, registry);
			try {
				CSVParser parser = CSVParser.parse(sourceUrl, Charset.forName("UTF-8"), csvFormat);
				csvIterator = parser.iterator();
				firstRecord = csvIterator.next();
			} catch (IOException ioe) {
				throw new SourceAdapterException(ioe);
			}
		}


		@Override
		protected boolean doHasNext() {
			return csvIterator.hasNext();
		}


		@Override
		protected ObjectNode doNext() {
			return createObject(firstRecord, csvIterator.next());
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
