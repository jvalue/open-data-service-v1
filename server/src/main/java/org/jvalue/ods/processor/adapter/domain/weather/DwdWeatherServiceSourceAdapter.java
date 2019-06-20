package org.jvalue.ods.processor.adapter.domain.weather;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.JsonSourceIterator;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.adapter.domain.weather.models.*;
import org.jvalue.ods.utils.JsonMapper;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static org.jvalue.commons.utils.Assert.assertNotNull;

public class DwdWeatherServiceSourceAdapter implements SourceAdapter {

	private static final String DWD_SERVICE_BASE_ADDRESS = "http://localhost:8090/api/v1";

	private final DataSource dataSource;
	private final MetricRegistry registry;
	private final Location location;
	private final String time;
	private final NodeParsingStrategy responseParser;

	/**
	 * Only uses the first location. No need for more than one. TODO make the user only input one location.
	 *
	 * @throws IllegalArgumentException if no Location is specified or time is neither current nor forecast.
	 */
	@Inject
	DwdWeatherServiceSourceAdapter(
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_LOCATION) LinkedHashMap<String, String> location,
		@Assisted(SourceAdapterFactory.ARGUMENT_TIME) String time,
		MetricRegistry registry) {
		assertNotNull(location);
		assertNotNull(time);
		time = time.trim().toLowerCase();
		checkTimeParameter(time);

		this.dataSource = dataSource;
		this.registry = registry;
		this.location = JsonMapper.convertValue(location, Location.class);
		this.time = time;
		this.responseParser = selectResponseParser(this.time);
	}

	@Override
	@Nonnull
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {
		Iterator<ObjectNode> nodeIterator = new JsonSourceIterator(dataSource, createSourceUrl(time, location), registry);
		return responseParser.parseServiceResponse(nodeIterator);
	}

	/**
	 * @return the URL for the required request to fetch weather data in time for location.
	 */
	private URL createSourceUrl(String time, Location location) {
		URI baseUri = URI.create(DWD_SERVICE_BASE_ADDRESS);
		UriBuilder builder = UriBuilder.fromUri(baseUri).path(time);
		builder = addLocationQueryParam(builder, location);
		URI resultUri = builder.build();
		try {
			return resultUri.toURL();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url " + resultUri);
		}
	}

	/**
	 * Interface for current and forecast response parsing strategies.
	 */
	private interface NodeParsingStrategy {
		Iterator<ObjectNode> parseServiceResponse(Iterator<ObjectNode> nodeIterator);
	}

	/**
	 * NodeParsingStrategy for current time.
	 */
	private class CurrentResponseParser implements NodeParsingStrategy {
		public Iterator<ObjectNode> parseServiceResponse(Iterator<ObjectNode> nodeIterator) {
			if (!nodeIterator.hasNext()) return Collections.emptyIterator();

			ObjectNode node = nodeIterator.next();

			String stationId = "unknown";
			Instant timestamp = Instant.parse(node.get("time").asText());

			JsonNode weatherNode = node.get("weather");

			JsonNode temperatureNode = findDataPointNodeByName(weatherNode, "temperature200");
			Temperature temperature = null;
			if (temperatureNode != null) {
				double temperatureValue = temperatureNode.get("value").asDouble();
				temperature = new Temperature(TemperatureType.CELSIUS.fromKelvin(temperatureValue), TemperatureType.CELSIUS);
			}

			JsonNode airPressureNode = findDataPointNodeByName(weatherNode, "air_pressure");
			Pressure pressure = null;
			if (airPressureNode != null) {
				double pressureValue = airPressureNode.get("value").asDouble();
				pressure = new Pressure((int) pressureValue, PressureType.H_PA);
			}

			//TODO this is currently not delivered in current, but could be if the service differentiates between current and forecast standard parameters.
			JsonNode humidityNode = findDataPointNodeByName(weatherNode, "humidity");
			int humidityInPercent = -1;
			if (humidityNode != null) {
				double humidityValue = humidityNode.get("value").asDouble();
				humidityInPercent = (int) Math.round(humidityValue);
			}

			Weather weather = new Weather(//TODO extend the weather model to support all standard parameters
				stationId,
				temperature,
				pressure,
				humidityInPercent,
				timestamp,
				location);

			ObjectNode resultNode = JsonMapper.valueToTree(weather);
			ArrayList<ObjectNode> resultList = new ArrayList<>();
			resultList.add(resultNode);
			return resultList.iterator();
		}

	}

	/**
	 * @return the JsonNode where the "name" field is set to name.
	 */
	private JsonNode findDataPointNodeByName(JsonNode node, String name) {
		for (JsonNode subNode : node) {
			if (name.equals(subNode.get("name").asText())) {
				return subNode;
			}
		}
		return null;
	}

	/**
	 * @return the builder with either lat and lon or city query parameter set to location.
	 */
	private UriBuilder addLocationQueryParam(UriBuilder builder, Location location) {
		if (location.hasCoordinate()) {
			return builder.queryParam("lat", location.getCoordinate().getLatitude())
				.queryParam("lon", location.getCoordinate().getLongitude());
		} else if (location.hasZipCode()) {
			return builder.queryParam("city", location.getZipCode());
		} else if (location.hasCity()) {
			return builder.queryParam("city", location.getCity());
		} else {
			throw new IllegalArgumentException("Location must not be empty");
		}
	}

	/**
	 * Based on the requested time, different responses require different parsers.
	 */
	private NodeParsingStrategy selectResponseParser(String time) {
		switch (time) {
			case "forecast"://TODO
			default:
				return new CurrentResponseParser();
		}
	}

	/**
	 * @throws IllegalArgumentException if time is neither current nor forecast.
	 */
	private void checkTimeParameter(String time) throws IllegalArgumentException {
		if (!(time.equals("current") || time.equals("forecast"))) {
			throw new IllegalArgumentException("time parameter must either be current or forecast");
		}
	}
}
