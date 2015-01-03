package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;

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
				return jsonParser.hasCurrentToken() && jsonParser.getCurrentToken() == JsonToken.START_OBJECT;
			} catch (IOException e) {
				throw new SourceAdapterException(e);
			}
		}


		@Override
		protected ObjectNode doNext() {
			try {
				if (jsonParser == null) initJsonParser();
				ObjectNode node = mapper.readTree(jsonParser);
				jsonParser.nextToken();
				jsonParser.nextToken();
				return node;
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
