package org.jvalue.ods.processor.adapter.domain.weather;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.JsonSourceIterator;
import org.jvalue.ods.processor.adapter.SourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceAdapterFactory;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

final public class OpenWeatherMapSourceAdapter implements SourceAdapter {

	private final ObjectMapper mapper = new ObjectMapper();
	private final DataSource dataSource;
	private final MetricRegistry registry;
	private final List<Location> locations;
	private final String apiKey;
	private final String countryCode = "de";

	@Inject
	OpenWeatherMapSourceAdapter(
		@Assisted DataSource dataSource,
		@Assisted(SourceAdapterFactory.ARGUMENT_LOCATIONS) ArrayList<LinkedHashMap<String, String>> locations,
		@Assisted(SourceAdapterFactory.ARGUMENT_API_KEY) String apiKey,
		MetricRegistry registry) {

		this.dataSource = dataSource;
		this.registry = registry;
		this.apiKey = apiKey;

		this.locations = locations.stream()
			.map(l -> mapper.convertValue(l, Location.class))
			.collect(Collectors.toList());
	}


	@Override
	public Iterator<ObjectNode> iterator() throws SourceAdapterException {
		List<ObjectNode> result = new ArrayList<>();
		for (Location location : locations) {
			Iterator<ObjectNode> nodeIterator = new JsonSourceIterator(dataSource, createSourceUrl(location), registry);
			if (nodeIterator.hasNext()) {
				ObjectNode node = nodeIterator.next();
				result.add(node);
			}
		}

		return result.iterator();
	}


	private URL createSourceUrl(Location location) {
		URI baseUri = URI.create("https://api.openweathermap.org/data/2.5/weather");
		UriBuilder builder = UriBuilder.fromUri(baseUri)
			.queryParam("APPID", apiKey)
			.queryParam("units", "metric")
			.queryParam("lang", countryCode);
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
			builder.queryParam("lat", location.getCoordinate().getLatitude());
			builder.queryParam("lon", location.getCoordinate().getLongitude());
		} else if (location.hasZipCode()) {
			builder.queryParam("zip", location.getZipCode() + "," + countryCode);
		} else if (location.hasCity() ) {
			builder.queryParam("q", location.getCity() + "," + countryCode);
		} else {
			throw new IllegalArgumentException("Location must not be empty");
		}
		return builder;
	}

}
