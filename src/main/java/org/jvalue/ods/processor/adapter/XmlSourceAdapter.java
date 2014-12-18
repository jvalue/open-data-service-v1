package org.jvalue.ods.processor.adapter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;

import java.io.IOException;
import java.util.NoSuchElementException;


final class XmlSourceAdapter extends AbstractSourceAdapter {

	@Inject
	XmlSourceAdapter(@Assisted DataSource source) {
		super(source);
	}


	@Override
	protected SourceIterator doCreateIterator(DataSource source) {
		return new XmlSourceIterator(source);
	}


	private static final class XmlSourceIterator extends SourceIterator {

		private static final ObjectMapper mapper = new ObjectMapper();
		private final DataSource source;
		private JsonParser jsonParser;

		public XmlSourceIterator(DataSource source) {
			this.source = source;
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
				if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) throw new NoSuchElementException();
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
				jsonParser = new XmlFactory().createParser(source.getUrl());
				if (jsonParser.nextToken() != JsonToken.START_OBJECT) throw new IllegalStateException("xml should start with an object");
				jsonParser.nextToken();
				jsonParser.nextToken();
			}
		}

	}

}
