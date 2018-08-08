package org.jvalue.ods.processor.adapter.domain.weather;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.adapter.SourceIterator;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

final public class OpenWeatherMapSourceAdapter implements SourceAdapter {

	private final DataSource dataSource;
	private final URL sourceUrl;
	private final MetricRegistry registry;

	@Inject
	OpenWeatherMapSourceAdapter(
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_LOCATIONS) String locations,
		@Assisted(SourceAdapterFactory.ARGUMENT_API_KEY) String apiKey,
		MetricRegistry registry) {

		this.dataSource = dataSource;
		this.registry = registry;
		this.sourceUrl = createSourceUrl(locations, apiKey);
	}


	@Override
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {
		return new JsonSourceIterator(dataSource, sourceUrl, registry);
	}


	private URL createSourceUrl(String location, String apiKey) {
		URI baseUri = URI.create("https://api.openweathermap.org/data/2.5/weather");
		URI resultUri = UriBuilder.fromUri(baseUri)
			.queryParam("q", location)
			.queryParam("APPID", apiKey)
			.queryParam("units", "metric")
			.queryParam("lang", "de")
			.build();

		try {
			return resultUri.toURL();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url " + resultUri);
		}
	}


	private static final class JsonSourceIterator extends SourceIterator {

		private static final ObjectMapper mapper = new ObjectMapper();
		private JsonParser jsonParser;

		JsonSourceIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
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
}
