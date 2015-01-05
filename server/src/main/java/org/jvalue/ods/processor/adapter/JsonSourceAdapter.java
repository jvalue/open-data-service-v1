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

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.net.URL;


final class JsonSourceAdapter extends AbstractSourceAdapter {


	@Inject
	JsonSourceAdapter(@Assisted DataSource source, @Assisted String sourceUrl, MetricRegistry registry) {
		super(source, sourceUrl, registry);
	}


	@Override
	protected SourceIterator doCreateIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		return new JsonSourceIterator(source, sourceUrl, registry);
	}


	private static final class JsonSourceIterator extends SourceIterator {

		private static final ObjectMapper mapper = new ObjectMapper();
		private JsonParser jsonParser;

		public JsonSourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
			super(source, sourceUrl, registry);
		}


		@Override
		protected boolean doHasNext() {
			try {
				initJsonParser();
				return jsonParser.hasCurrentToken();
			} catch (IOException e) {
				throw new SourceAdapterException(e);
			}
		}


		@Override
		protected ObjectNode doNext() {
			try {
				initJsonParser();
				jsonParser.nextToken();
				return mapper.readTree(jsonParser);
			} catch (IOException e) {
				throw new SourceAdapterException(e);
			}
		}


		private void initJsonParser() throws IOException {
			if (jsonParser == null) {
				jsonParser = new JsonFactory().createParser(sourceUrl);
				if (jsonParser.nextToken() != JsonToken.START_ARRAY)
					throw new IllegalStateException("json should start with array");
				jsonParser.nextToken();
			}
		}
	}


}
