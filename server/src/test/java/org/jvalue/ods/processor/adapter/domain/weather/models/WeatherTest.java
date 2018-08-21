package org.jvalue.ods.processor.adapter.domain.weather.models;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.utils.JsonMapper;

import java.io.IOException;
import java.time.Instant;

public class WeatherTest {

	private final Temperature temperature = new Temperature(32.0, TemperatureType.CELSIUS);
	private final Pressure pressure = new Pressure(1020, PressureType.H_PA);
	private final Location location = new Location("Berlin", Location.UNKNOWN, null);
	private final Instant timestamp = Instant.ofEpochSecond(1534332000);

	private final Weather weather = new Weather(
		"42",
		temperature,
		pressure,
		45,
		timestamp,
		location);


	@Test
	public void testConstructor() {
		Assert.assertEquals("42", weather.getStationId());
		Assert.assertEquals(temperature, weather.getTemperature());
		Assert.assertEquals(pressure, weather.getPressure());
		Assert.assertEquals(45, weather.getHumidityInPercent());
		Assert.assertEquals(timestamp, weather.getTimestamp());
		Assert.assertEquals(location, weather.getLocation());
	}


	@Test
	public void testEquals() {
		Temperature otherTemp = new Temperature(32.0, TemperatureType.CELSIUS);
		Pressure otherPress = new Pressure(1020, PressureType.H_PA);
		Location otherLoc = new Location("Berlin", Location.UNKNOWN, null);
		Weather other = new Weather("42", otherTemp, otherPress, 45, timestamp,	otherLoc);

		Assert.assertEquals(weather, other);
	}


	@Test
	public void testDeserialization() throws IOException {
		String weatherStr = JsonMapper.writeValueAsString(weather);

		Weather result = JsonMapper.readValue(weatherStr, Weather.class);

		Assert.assertEquals(weather, result);
	}

}
