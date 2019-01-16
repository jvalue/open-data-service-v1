/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.net.URL;


final class XmlSourceAdapter extends AbstractSourceAdapter {

	@Inject
	XmlSourceAdapter(@Assisted DataSource source, @Assisted String sourceUrl, MetricRegistry registry) {
		super(source, sourceUrl, registry);
	}


	@Override
	protected SourceIterator doCreateIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		return new XmlSourceIterator(source, sourceUrl, registry);
	}


	private static final class XmlSourceIterator extends SourceIterator {

		private static final ObjectMapper mapper = new ObjectMapper();
		private JsonParser jsonParser;

		public XmlSourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
			super(source, sourceUrl, registry);
		}


		@Override
		protected boolean doHasNext() {
			try {
				if (jsonParser == null) initJsonParser();
				return jsonParser.hasCurrentToken();
			} catch (IOException e) {
				throw new SourceAdapterException(e);
			}
		}


		@Override
		protected JsonNode doNext() {
			try {
				if (jsonParser == null) initJsonParser();
				JsonNode data = mapper.readTree(jsonParser);
				jsonParser.nextToken();
				jsonParser.nextToken();
				return data;
			} catch (IOException e) {
				throw new SourceAdapterException(e);
			}
		}


		private void initJsonParser() throws IOException {
			if (jsonParser == null) {
				jsonParser = new XmlFactory().createParser(sourceUrl);
				if (jsonParser.nextToken() != JsonToken.START_OBJECT) throw new IllegalStateException("xml should start with an object");
				jsonParser.nextToken();
				jsonParser.nextToken();
			}
		}

	}

}
