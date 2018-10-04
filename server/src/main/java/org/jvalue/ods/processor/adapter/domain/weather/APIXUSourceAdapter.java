package org.jvalue.ods.processor.adapter.domain.weather;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.JsonSourceIterator;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.adapter.domain.weather.models.*;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public final class APIXUSourceAdapter implements SourceAdapter {

	private String alias = this.getClass().getSimpleName();
	private final ObjectMapper mapper = new ObjectMapper();
	private final DataSource dataSource;
	private final MetricRegistry registry;
	private final List<Location> locations;
	private final String apiKey;

	@Inject
	APIXUSourceAdapter(
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_LOCATIONS) ArrayList<LinkedHashMap<String, String>> locations,
		@Assisted(SourceAdapterFactory.ARGUMENT_API_KEY) String apiKey,
		MetricRegistry registry
	) {
		this.dataSource = dataSource;
		this.registry = registry;
		this.apiKey = apiKey;

		this.locations = locations.stream()
			.map(l -> mapper.convertValue(l, Location.class))
			.collect(Collectors.toList());
	}


	@Override
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {
		List<ObjectNode> result = new ArrayList<>();
		for (Location location : locations) {
			Iterator<ObjectNode> nodeIterator = new JsonSourceIterator(dataSource, createSourceUrl(location), registry);
			if (nodeIterator.hasNext()) {
				ObjectNode node = nodeIterator.next();
				Weather weather = createWeatherFromObjectNode(node);
				ObjectNode weatherNode = mapper.valueToTree(weather);
				result.add(weatherNode);
			}
		}

		return result.iterator();
	}


	@Override
	public void setAlias(String name) {
		this.alias = name;
	}


	@Override
	public String getAlias() {
		return this.alias;
	}


	private URL createSourceUrl(Location location) {
		URI baseUri = URI.create("http://api.apixu.com/v1/current.json");
		UriBuilder builder = UriBuilder.fromUri(baseUri)
			.queryParam("key", apiKey)
			.queryParam("lang", location.getCountryCode());
		builder = addLocationQueryParam(builder, location);
		URI resultUri = builder.build();

		try {
			return resultUri.toURL();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url " + resultUri);
		}
	}


	private UriBuilder addLocationQueryParam(UriBuilder builder, Location location) {
		if (location.hasCoordinate()) {
			String coordinate = location.getCoordinate().getLatitude() + "," + location.getCoordinate().getLongitude();
			builder.queryParam("q", coordinate);
		} else if (location.hasZipCode()) {
			builder.queryParam("q", location.getZipCode() );
		} else if (location.hasCity() ) {
			builder.queryParam("q", location.getCity());
		} else {
			throw new IllegalArgumentException("Location must not be empty");
		}
		return builder;
	}


	private Weather createWeatherFromObjectNode(ObjectNode node) {
		String city = node.get("location").get("alias").asText();
		JsonNode current = node.get("current");
		double temperatureValue = current.get("temp_c").doubleValue();
		Temperature temperature = new Temperature(temperatureValue, TemperatureType.CELSIUS);

		Pressure pressure = new Pressure(current.get("pressure_mb").doubleValue(), PressureType.M_BAR);

		int humidityInPercent = current.get("humidity").intValue();

		Instant timestamp = Instant.ofEpochSecond(current.get("last_updated_epoch").asInt());

		Coordinate coordinate = new Coordinate(
			node.get("location").get("lat").asDouble(),
			node.get("location").get("lon").asDouble()
		);
		Location location = new Location(
			city,
			Location.UNKNOWN,
			coordinate,
			Location.UNKNOWN
		);

		return new Weather(
			city,
			temperature,
			pressure,
			humidityInPercent,
			timestamp,
			location);
	}

}
