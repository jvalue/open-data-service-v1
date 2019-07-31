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
import org.jvalue.ods.processor.adapter.domain.weather.models.extended.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.jvalue.commons.utils.Assert.assertNotNull;

public class DwdWeatherServiceSourceAdapter implements SourceAdapter {

	private static final String DWD_SERVICE_BASE_ADDRESS = "http://localhost:8090/api/v1";

	private final ObjectMapper mapper = new ObjectMapper();
	private final DataSource dataSource;
	private final MetricRegistry registry;
	private final List<Location> locations;

	/**
	 * @throws IllegalArgumentException if no Location is specified or mode is neither current nor forecast.
	 */
	@Inject
	DwdWeatherServiceSourceAdapter(
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_LOCATIONS) ArrayList<LinkedHashMap<String, String>> locations,
		MetricRegistry registry) {
		assertNotNull(locations);

		this.dataSource = dataSource;
		this.registry = registry;

		this.locations = locations.stream()
			.map(l -> mapper.convertValue(l, Location.class))
			.collect(Collectors.toList());
	}

	@Override
	@Nonnull
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {
		List<ObjectNode> result = new ArrayList<>();
		for (Location location : locations) {
			Iterator<ObjectNode> nodeIterator = new JsonSourceIterator(dataSource, createSourceUrl(location), registry);
			if (nodeIterator.hasNext()) {
				ObjectNode node = nodeIterator.next();
				Weather weather = parseServiceResponse(node, location);
				ObjectNode weatherNode = mapper.valueToTree(weather);
				result.add(weatherNode);
			}
		}

		return result.iterator();
	}

	/**
	 * @return the URL for the required request to fetch current weather data for location.
	 */
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
	 * @return service results as weather model representation
	 */
	private Weather parseServiceResponse(ObjectNode node, Location location) {
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

		JsonNode humidityNode = findDataPointNodeByName(weatherNode, "humidity");
		int humidityInPercent = -1;
		if (humidityNode != null) {
			double humidityValue = humidityNode.get("value").asDouble();
			humidityInPercent = (int) Math.round(humidityValue);
		}

		JsonNode precipitationDurationNode = findDataPointNodeByName(weatherNode, "precipitation_duration");
		PrecipitationDuration precipitationDuration = null;
		if (precipitationDurationNode != null) {
			double precipitationDurationValue = precipitationDurationNode.get("value").asDouble();
			precipitationDuration = new PrecipitationDuration(precipitationDurationValue, DurationType.SECOND);
		}

		JsonNode precipitationHeightNode = findDataPointNodeByName(weatherNode, "precipitation_height");
		PrecipitationHeight precipitationHeight = null;
		if (precipitationHeightNode != null) {
			double precipitationHeightValue = precipitationHeightNode.get("value").asDouble();
			precipitationHeight = new PrecipitationHeight(precipitationHeightValue, LengthType.MILLIMETER);
		}

		JsonNode sunshineDurationNode = findDataPointNodeByName(weatherNode, "sunshine_duration");
		SunshineDuration sunshineDuration = null;
		if (sunshineDurationNode != null) {
			double sunshineDurationValue = sunshineDurationNode.get("value").asDouble();
			sunshineDuration = new SunshineDuration(sunshineDurationValue, DurationType.SECOND);
		}

		JsonNode windSpeedNode = findDataPointNodeByName(weatherNode, "wind_speed");
		WindSpeed windSpeed = null;
		if (windSpeedNode != null) {
			double windSpeedValue = windSpeedNode.get("value").asDouble();
			windSpeed = new WindSpeed(windSpeedValue, SpeedType.METER_PER_SECOND);
		}

		JsonNode windDirectionNode = findDataPointNodeByName(weatherNode, "wind_direction");
		WindDirection windDirection = null;
		if (windDirectionNode != null) {
			double windDirectionValue = windDirectionNode.get("value").asDouble();
			windDirection = new WindDirection(windDirectionValue, AngleType.DEGREE);
		}

		JsonNode cloudCoverInPercentNode = findDataPointNodeByName(weatherNode, "cloud_cover");
		int cloudCoverInPercent = -1;
		if (cloudCoverInPercentNode != null) {
			double cloudCoverInPercentValue = cloudCoverInPercentNode.get("value").asDouble();
			humidityInPercent = (int) Math.round(cloudCoverInPercentValue);
		}

		JsonNode totalSolarRadiationNode = findDataPointNodeByName(weatherNode, "total_solar_radiation");
		TotalSolarRadiation totalSolarRadiation = null;
		if (totalSolarRadiationNode != null) {
			double totalSolarRadiationValue = totalSolarRadiationNode.get("value").asDouble();
			totalSolarRadiation = new TotalSolarRadiation(totalSolarRadiationValue, RadiationType.JOULE_PER_SQUARE_CENTIMETER);
		}

		ExtendedWeather weather = new ExtendedWeather(
			String.valueOf(location.hashCode()),
			temperature,
			pressure,
			humidityInPercent,
			precipitationDuration,
			precipitationHeight,
			sunshineDuration,
			windSpeed,
			windDirection,
			cloudCoverInPercent,
			totalSolarRadiation,
			timestamp,
			location);

		return weather;
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

}
