package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jvalue.ods.api.sources.DataSource;

import java.io.IOException;
import java.net.URL;

public final class JsonSourceIterator extends SourceIterator{

	private static final ObjectMapper mapper = new ObjectMapper();
	private JsonParser jsonParser;

	public JsonSourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		super(source, sourceUrl, registry);
	}


	@Override
	protected boolean doHasNext() {
		try {
			initParserIfNotExist();
			return hasToken(jsonParser);
		} catch (IOException e) {
			throw new SourceAdapterException(e);
		}
	}


	@Override
	protected JsonNode doNext() {
		try {
			initParserIfNotExist();
			return getNode();
		} catch (IOException e) {
			throw new SourceAdapterException(e);
		}
	}


	private void initParserIfNotExist() throws IOException{
		if (jsonParser == null) {
			jsonParser = new JsonFactory().createParser(sourceUrl);
			jsonParser.nextToken();
		}
	}


	private JsonNode getNode() throws IOException {
		assertIsValidJsonToken(jsonParser.getCurrentToken());

		if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
			jsonParser.nextToken();
		}
		JsonNode node = mapper.readTree(jsonParser);
		jsonParser.nextToken();

		return node;
	}


	private boolean hasToken(JsonParser parser) {
		return parser.hasCurrentToken()
			&& parser.getCurrentToken() != JsonToken.END_ARRAY
			&& parser.getCurrentToken() != JsonToken.END_OBJECT;
	}


	private void assertIsValidJsonToken(JsonToken token) {
		if (token != JsonToken.START_ARRAY && token != JsonToken.START_OBJECT) {
			throw new IllegalArgumentException("Json should start with array or object identifier");
		}
	}
}
