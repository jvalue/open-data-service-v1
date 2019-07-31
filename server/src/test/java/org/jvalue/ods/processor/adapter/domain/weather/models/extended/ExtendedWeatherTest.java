package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.processor.adapter.domain.weather.models.*;
import org.jvalue.ods.utils.JsonMapper;

import java.io.IOException;
import java.time.Instant;

public class ExtendedWeatherTest {

	private final Temperature temperature = new Temperature(32.0, TemperatureType.CELSIUS);
	private final Pressure pressure = new Pressure(1020, PressureType.H_PA);
	private final PrecipitationDuration precipitationDuration = new PrecipitationDuration(1.0, DurationType.HOUR);
	private final PrecipitationHeight precipitationHeight = new PrecipitationHeight(2.0, LengthType.CENTIMETER);
	private final SunshineDuration sunshineDuration = new SunshineDuration(3.14, DurationType.HOUR);
	private final WindSpeed windSpeed = new WindSpeed(1.1, SpeedType.KILOMETER_PER_HOUR);
	private final WindDirection windDirection = new WindDirection(45.0, AngleType.DEGREE);
	private final int cloudCoverInPercent = 12;
	private final TotalSolarRadiation totalSolarRadiation = new TotalSolarRadiation(38.9, RadiationType.JOULE_PER_SQUARE_CENTIMETER);
	private final Location location = new Location("Erlangen", Location.UNKNOWN, null);
	private final Instant timestamp = Instant.ofEpochSecond(1534332000);

	private final ExtendedWeather weather = new ExtendedWeather(
		"42",
		temperature,
		pressure,
		45,
		precipitationDuration,
		precipitationHeight,
		sunshineDuration,
		windSpeed,
		windDirection,
		cloudCoverInPercent,
		totalSolarRadiation,
		timestamp,
		location
	);


	@Test
	public void testDeserialization_Json() throws IOException {
		String weatherStr = JsonMapper.writeValueAsString(weather);

		System.out.println(weatherStr);
		ExtendedWeather result = JsonMapper.readValue(weatherStr, ExtendedWeather.class);

		Assert.assertEquals(weather, result);
	}


	@Test
	public void testDeserialization_JsonNode() {
		ObjectNode node = JsonMapper.valueToTree(weather);

		ExtendedWeather result = JsonMapper.convertValue(node, ExtendedWeather.class);

		Assert.assertEquals(weather, result);
	}


	@Test
	public void testEquals() {
		Temperature temperature = new Temperature(32.0, TemperatureType.CELSIUS);
		Pressure pressure = new Pressure(1020, PressureType.H_PA);
		PrecipitationDuration precipitationDuration = new PrecipitationDuration(1.0, DurationType.HOUR);
		PrecipitationHeight precipitationHeight = new PrecipitationHeight(2.0, LengthType.CENTIMETER);
		SunshineDuration sunshineDuration = new SunshineDuration(3.14, DurationType.HOUR);
		WindSpeed windSpeed = new WindSpeed(1.1, SpeedType.KILOMETER_PER_HOUR);
		WindDirection windDirection = new WindDirection(45.0, AngleType.DEGREE);
		int cloudCoverInPercent = 12;
		TotalSolarRadiation totalSolarRadiation = new TotalSolarRadiation(38.9, RadiationType.JOULE_PER_SQUARE_CENTIMETER);
		Location location = new Location("Erlangen", Location.UNKNOWN, null);
		Instant timestamp = Instant.ofEpochSecond(1534332000);

		ExtendedWeather other = new ExtendedWeather(
			"42",
			temperature,
			pressure,
			45,
			precipitationDuration,
			precipitationHeight,
			sunshineDuration,
			windSpeed,
			windDirection,
			cloudCoverInPercent,
			totalSolarRadiation,
			timestamp,
			location
		);

		Assert.assertEquals(weather, other);
	}
}
