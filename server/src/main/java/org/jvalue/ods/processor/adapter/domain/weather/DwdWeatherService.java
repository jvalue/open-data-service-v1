package org.jvalue.ods.processor.adapter.domain.weather;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.JsonSourceIterator;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;
import org.jvalue.ods.processor.adapter.domain.weather.models.Location;
import org.jvalue.ods.processor.adapter.domain.weather.models.Weather;
import org.jvalue.ods.utils.JsonMapper;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class DwdWeatherService implements SourceAdapter {

	private static final String DWD_SERVICE_BASE_ADDRESS = "localhost:8080/api/v1";

	private final DataSource dataSource;
	private final MetricRegistry registry;
	private final Location location;

	/**
	 * Only uses the first location. No need for more than one. TODO make the user only input one location.
	 *
	 * @throws IllegalArgumentException if no Location is specified.
	 */
	@Inject
	DwdWeatherService(
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_LOCATIONS) ArrayList<LinkedHashMap<String, String>> locations,
		MetricRegistry registry) {

		this.dataSource = dataSource;
		this.registry = registry;

		Optional<Location> optionalLocation = locations.stream()
			.map(l -> JsonMapper.convertValue(l, Location.class))
			.findFirst();

		if (!optionalLocation.isPresent()) {
			throw new IllegalArgumentException("We need a location to fetch weather data from a nearby weather station");
		}
		this.location = optionalLocation.get();
	}

	@Override
	@Nonnull
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {

		List<ObjectNode> result = new ArrayList<>();

		Iterator<ObjectNode> nodeIterator = new JsonSourceIterator(dataSource, createSourceUrl(location), registry);
		if (nodeIterator.hasNext()) {
			ObjectNode node = nodeIterator.next();
			Weather weather = null;//TODO createWeatherFromObjectNode(node);
			ObjectNode weatherNode = JsonMapper.valueToTree(weather);
			result.add(weatherNode);
		}
		return result.iterator();
	}

	/**
	 * TODO only supports current weather at the moment.
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
}
