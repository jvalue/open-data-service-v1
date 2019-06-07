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
import java.util.*;

public class DwdWeatherServiceSourceAdapter implements SourceAdapter {

	private static final String DWD_SERVICE_BASE_ADDRESS = "http://localhost:8090/api/v1";

	private final DataSource dataSource;
	private final MetricRegistry registry;
	private final Location location;
	private final String time;
//TODO add parameter

	/**
	 * Only uses the first location. No need for more than one. TODO make the user only input one location.
	 *
	 * @throws IllegalArgumentException if no Location is specified.
	 */
	@Inject
	DwdWeatherServiceSourceAdapter(
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_LOCATION) LinkedHashMap<String, String> location,
		@Assisted(SourceAdapterFactory.ARGUMENT_TIME) String time,
		MetricRegistry registry) {

		this.dataSource = dataSource;
		this.registry = registry;
		this.location = JsonMapper.convertValue(location, Location.class);
		this.time = time;
	}

	@Override
	@Nonnull
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {

		List<ObjectNode> result = new ArrayList<>();

		Iterator<ObjectNode> nodeIterator = new JsonSourceIterator(dataSource, createSourceUrl(location, time), registry);
		if (nodeIterator.hasNext()) {
			ObjectNode node = nodeIterator.next();
			System.out.println(node);
			Weather weather = createWeatherFromCurrentObjectNode(node);    //TODO only current weather
			ObjectNode weatherNode = JsonMapper.valueToTree(weather);
			result.add(weatherNode);
		}
		return result.iterator();
	}

	/**
	 * TODO only supports current weather at the moment.
	 */
	private URL createSourceUrl(Location location, String time) {
		if (time == null) {
			return createSourceUrl(location);
		}
		//TODO use time
		return createSourceUrl(location);
	}

	private URL createSourceUrl(Location location) {
		URI baseUri = URI.create(DWD_SERVICE_BASE_ADDRESS);
		UriBuilder builder = UriBuilder.fromUri(baseUri).path("current");
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

	private Weather createWeatherFromCurrentObjectNode(ObjectNode node) {

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
			pressure = new Pressure((int) pressureValue, PressureType.H_PA);//TODO Pressure destroys itself when receiving a proper double.
		}

		//TODO this is currently not delivered in current, but could be if the service differentiates between current and forecast standard parameters.
		JsonNode humidityNode = findDataPointNodeByName(weatherNode, "humidity");
		int humidityInPercent = -1;
		if (humidityNode != null) {
			double humidityValue = humidityNode.get("value").asDouble();
			humidityInPercent = (int) Math.round(humidityValue);
		}

		return new Weather(
			stationId,
			temperature,
			pressure,
			humidityInPercent,
			timestamp,
			location);
	}

	private JsonNode findDataPointNodeByName(JsonNode node, String name) {
		for (JsonNode subNode : node) {
			if (name.equals(subNode.get("name").asText())) {
				return subNode;
			}
		}
		return null;
	}
}
