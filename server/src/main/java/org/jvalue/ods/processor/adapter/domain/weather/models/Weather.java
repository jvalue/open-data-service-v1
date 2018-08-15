package org.jvalue.ods.processor.adapter.domain.weather.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

public class Weather {

	private final String stationId;
	private final Temperature temperature;
	private final Pressure pressure;
	private final double humidityInPercent;
	private final Instant timestamp;
	private final Location location;

	public Weather(
		@JsonProperty("stationId") String stationId,
		@JsonProperty("temperature") Temperature temperature,
		@JsonProperty("pressure")Pressure pressure,
		@JsonProperty("humidityInPercent") int humidityInPercent,
		@JsonProperty("timestamp") Instant timestamp,
		@JsonProperty("location") Location location) {
		this.stationId = stationId;
		this.temperature = temperature;
		this.pressure = pressure;
		this.humidityInPercent = humidityInPercent;
		this.timestamp = timestamp;
		this.location = location;
	}

	public String getStationId() {
		return stationId;
	}


	public Temperature getTemperature() {
		return temperature;
	}


	public Pressure getPressure() {
		return pressure;
	}


	public double getHumidityInPercent() {
		return humidityInPercent;
	}


	public Instant getTimestamp() {
		return timestamp;
	}


	public Location getLocation() {
		return location;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Weather weather = (Weather) o;
		return Double.compare(weather.humidityInPercent, humidityInPercent) == 0 &&
			Objects.equals(stationId, weather.stationId) &&
			Objects.equals(temperature, weather.temperature) &&
			Objects.equals(pressure, weather.pressure) &&
			Objects.equals(timestamp, weather.timestamp) &&
			Objects.equals(location, weather.location);
	}


	@Override
	public int hashCode() {
		return Objects.hash(stationId, temperature, pressure, humidityInPercent, timestamp, location);
	}
}
