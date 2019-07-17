package org.jvalue.ods.processor.adapter.domain.weather.dwd;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.JsonSourceIterator;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.adapter.domain.weather.models.Location;
import org.jvalue.ods.utils.JsonMapper;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
			case "forecast":
				return new ForecastResponseParser();
			case "current":
			default:
				return new CurrentResponseParser(location);
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
