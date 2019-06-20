package org.jvalue.ods.processor.adapter.domain.weather.dwd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.ods.processor.adapter.domain.weather.models.*;
import org.jvalue.ods.utils.JsonMapper;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * NodeParsingStrategy for forecast time.
 */
public class ForecastResponseParser implements NodeParsingStrategy {
	private static final int FORECAST_LENGTH = 240;
	private static final String TEMPERATURE = "temperature200";
	private static final String PRESSURE = "air_pressure";


	@Override
	public Iterator<ObjectNode> parseServiceResponse(Iterator<ObjectNode> nodeIterator) {
		if (!nodeIterator.hasNext()) return Collections.emptyIterator();

		ObjectNode node = nodeIterator.next();
		Location location = parseLocation(node);

		JsonNode forecastNode = node.get("forecast");
		Map<Instant, Map<String, Double>> forecastData = parseForecast(forecastNode);
		Instant startTimeStamp = findEarliestTimeStamp(forecastNode);

		List<Weather> weatherList = combineToWeather(location, forecastData, startTimeStamp);

		return weatherList.stream()
			.map((weather -> (ObjectNode) JsonMapper.valueToTree(weather)))
			.iterator();
	}

	/**
	 * Retrieves the location from the base node.
	 */
	@Nonnull
	private Location parseLocation(@Nonnull JsonNode baseNode) {
		JsonNode coordinatesNode = baseNode.get("coordinates");
		double lat = coordinatesNode.get("lat").asDouble();
		double lon = coordinatesNode.get("lon").asDouble();
		Coordinate coordinate = new Coordinate(lat, lon);
		return new Location(null, null, coordinate);
	}

	/**
	 * Parses all forecast elements into a map timestamp -> (Map parameter name -> value).
	 *
	 * @param forecastNode: node called "forecast"
	 */
	@Nonnull
	private Map<Instant, Map<String, Double>> parseForecast(@Nonnull JsonNode forecastNode) {

		Map<Instant, Map<String, Double>> forecastMap = new HashMap<>();
		Iterator<JsonNode> forecastIt = forecastNode.elements();
		while (forecastIt.hasNext()) {
			JsonNode paramForecastNode = forecastIt.next();

			String parameterName = paramForecastNode.get("parameter").get("name").asText();
			Map<Instant, Double> dataMap = parseData(paramForecastNode.get("data"));

			for (Map.Entry<Instant, Double> entry : dataMap.entrySet()) {
				forecastMap.putIfAbsent(entry.getKey(), new HashMap<>());
				Map<String, Double> timeMap = forecastMap.get(entry.getKey());
				timeMap.put(parameterName, entry.getValue());
			}
		}

		return forecastMap;
	}

	/**
	 * Parses the array of measurements and maps the timestamp to the measured value.
	 *
	 * @param dataNode: node called "data" inside "forecast"
	 */
	@Nonnull
	private Map<Instant, Double> parseData(@Nonnull JsonNode dataNode) {
		Map<Instant, Double> dataMap = new HashMap<>(FORECAST_LENGTH);

		Iterator<JsonNode> dataIt = dataNode.elements();
		while (dataIt.hasNext()) {
			JsonNode datumNode = dataIt.next();
			Instant timestamp = Instant.parse(datumNode.get("time").asText());
			Double value = datumNode.get("value").asDouble();
			dataMap.put(timestamp, value);
		}

		return dataMap;
	}

	/**
	 * @param forecastNode: node called "forecast"
	 * @return the earliest timeStamp of all parameters.
	 */
	@Nonnull
	private Instant findEarliestTimeStamp(@Nonnull JsonNode forecastNode) {
		Instant earliestTimeStamp = Instant.MAX;

		Iterator<JsonNode> forecastIt = forecastNode.elements();
		while (forecastIt.hasNext()) {
			JsonNode paramForecastNode = forecastIt.next();
			JsonNode dataNode = paramForecastNode.get("data");
			JsonNode firstMeasurementNode = dataNode.get(0);

			if (firstMeasurementNode != null) {
				Instant timestamp = Instant.parse(firstMeasurementNode.get("time").asText());

				if (timestamp.isBefore(earliestTimeStamp)) {
					earliestTimeStamp = timestamp;
				}
			}
		}

		return earliestTimeStamp;
	}

	/**
	 * Combines all data to a list of Weather objects.
	 */
	@Nonnull
	private List<Weather> combineToWeather(@Nonnull Location location,
										   @Nonnull Map<Instant, Map<String, Double>> data,
										   @Nonnull Instant startTimeStamp) {
		List<Weather> weatherList = new ArrayList<>(FORECAST_LENGTH);

		for (int i = 0; i < FORECAST_LENGTH; i++) {
			Instant timeStamp = startTimeStamp.plus(i, ChronoUnit.HOURS);
			Map<String, Double> parameterMap = data.get(timeStamp);
			if (parameterMap == null) {
				continue;
			}
			weatherList.add(getWeather(parameterMap, timeStamp, location));
		}

		return weatherList;
	}

	/**
	 * Put together all values to a Weather object.
	 */
	@Nonnull
	private Weather getWeather(@Nonnull Map<String, Double> parameterMap,
							   @Nonnull Instant timeStamp,
							   @Nonnull Location location) {
		Temperature temperature = null;
		if (parameterMap.containsKey(TEMPERATURE)) {
			double temperatureValue = parameterMap.get(TEMPERATURE);
			temperature = new Temperature(TemperatureType.CELSIUS.fromKelvin(temperatureValue), TemperatureType.CELSIUS);
		}

		Pressure pressure = null;
		if (parameterMap.containsKey(PRESSURE)) {
			double pressureValue = parameterMap.get(PRESSURE);
			pressure = new Pressure((int) pressureValue, PressureType.H_PA);
		}

		//TODO extend the weather model to feature all available parameters.

		return new Weather(null,
			temperature,
			pressure,
			-1,
			timeStamp,
			location);
	}
}
