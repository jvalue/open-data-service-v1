package org.jvalue.ods.processor.adapter.domain.weather.dwd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.ods.processor.adapter.domain.weather.models.*;
import org.jvalue.ods.utils.JsonMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * NodeParsingStrategy for current time.
 */
class CurrentResponseParser implements NodeParsingStrategy {

	private final Location location;

	CurrentResponseParser(Location location) {
		this.location = location;
	}

	public Iterator<ObjectNode> parseServiceResponse(Iterator<ObjectNode> nodeIterator) {
		if (!nodeIterator.hasNext()) return Collections.emptyIterator();

		ObjectNode node = nodeIterator.next();

		String stationId = "unknown";//TODO set it to null?
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
