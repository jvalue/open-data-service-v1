package org.jvalue.ods.processor.adapter.domain.weather.dwd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.ods.processor.adapter.domain.weather.models.*;
import org.jvalue.ods.processor.adapter.domain.weather.models.extended.*;
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
			null,
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
