package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.processor.adapter.domain.weather.models.Location;
import org.jvalue.ods.processor.adapter.domain.weather.models.Pressure;
import org.jvalue.ods.processor.adapter.domain.weather.models.Temperature;
import org.jvalue.ods.processor.adapter.domain.weather.models.Weather;

import java.time.Instant;
import java.util.Objects;

public class ExtendedWeather extends Weather {

	private final PrecipitationDuration precipitationDuration;
	private final PrecipitationHeight precipitationHeight;
	private final SunshineDuration sunshineDuration;
	private final WindSpeed windSpeed;
	private final WindDirection windDirection;
	private final int cloudCoverInPercent;
	private final TotalSolarRadiation totalSolarRadiation;

	@JsonCreator
	public ExtendedWeather(
		@JsonProperty("stationId") String stationId,
		@JsonProperty("temperature") Temperature temperature,
		@JsonProperty("pressure") Pressure pressure,
		@JsonProperty("humidityInPercent") int humidityInPercent,
		@JsonProperty("precipitationDuration") PrecipitationDuration precipitationDuration,
		@JsonProperty("precipitationHeight") PrecipitationHeight precipitationHeight,
		@JsonProperty("sunshineDuration") SunshineDuration sunshineDuration,
		@JsonProperty("windSpeed") WindSpeed windSpeed,
		@JsonProperty("windDirection") WindDirection windDirection,
		@JsonProperty("cloudCoverInPercent") int cloudCoverInPercent,
		@JsonProperty("totalSolarRadiation") TotalSolarRadiation totalSolarRadiation,
		@JsonProperty("timestamp") Instant timestamp,
		@JsonProperty("location") Location location) {

		super(stationId, temperature, pressure, humidityInPercent, timestamp, location);
		this.precipitationDuration = precipitationDuration;
		this.precipitationHeight = precipitationHeight;
		this.sunshineDuration = sunshineDuration;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.cloudCoverInPercent = cloudCoverInPercent;
		this.totalSolarRadiation = totalSolarRadiation;
	}

	public PrecipitationDuration getPrecipitationDuration() {
		return precipitationDuration;
	}

	public PrecipitationHeight getPrecipitationHeight() {
		return precipitationHeight;
	}

	public SunshineDuration getSunshineDuration() {
		return sunshineDuration;
	}

	public WindSpeed getWindSpeed() {
		return windSpeed;
	}

	public WindDirection getWindDirection() {
		return windDirection;
	}

	public int getCloudCoverInPercent() {
		return cloudCoverInPercent;
	}

	public TotalSolarRadiation getTotalSolarRadiation() {
		return totalSolarRadiation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExtendedWeather)) return false;
		if (!super.equals(o)) return false;
		ExtendedWeather that = (ExtendedWeather) o;
		return cloudCoverInPercent == that.cloudCoverInPercent &&
			Objects.equals(precipitationDuration, that.precipitationDuration) &&
			Objects.equals(precipitationHeight, that.precipitationHeight) &&
			Objects.equals(sunshineDuration, that.sunshineDuration) &&
			Objects.equals(windSpeed, that.windSpeed) &&
			Objects.equals(windDirection, that.windDirection) &&
			Objects.equals(totalSolarRadiation, that.totalSolarRadiation);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			super.hashCode(),
			precipitationDuration,
			precipitationHeight,
			sunshineDuration,
			windSpeed,
			windDirection,
			cloudCoverInPercent,
			totalSolarRadiation);
	}
}
