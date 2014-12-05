package org.jvalue.ods.sources.govdata;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.filter.reference.FilterChainExecutionInterval;
import org.jvalue.ods.rest.client.ClientFactory;
import org.jvalue.ods.rest.client.DataClient;
import org.jvalue.ods.rest.client.DataSourceClient;
import org.jvalue.ods.rest.client.FilterChainClient;
import org.jvalue.ods.rest.model.DataSource;
import org.jvalue.ods.rest.model.Filter;
import org.jvalue.ods.rest.model.FilterChain;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public final class RostockTrashCans {

	private final ClientFactory clientFactory = new ClientFactory();
	private final DataSourceClient sourceClient = clientFactory.getDataSourceClient();
	private final FilterChainClient filterClient = clientFactory.getFilterChainClient();
	private final DataClient dataClient = clientFactory.getDataClient();


	@Test
	public void testTrashCanSource() throws Exception {
		final String sourceId = getClass().getSimpleName();
		final String filterId = "testFilter";

		{
			// add source
			final DataSource source = new DataSource();
			source.url = new URL("https://geo.sv.rostock.de/download/opendata/abfallbehaelter/abfallbehaelter.csv");
			source.metaData = new DataSourceMetaData("", "", "", "", "", "", "");
			source.domainIdKey = "/id";
			source.schema = new ObjectNode(JsonNodeFactory.instance);
			sourceClient.add(sourceId, source);
		}

		{
			// assert empty
			ArrayNode data = dataClient.getAll(sourceId);
			Assert.assertEquals(0, data.size());
		}

		{
			// add filter chain
			final FilterChain filterChain = new FilterChain();
			filterChain.filters = new LinkedList<>();
			filterChain.executionInterval = new FilterChainExecutionInterval(100, TimeUnit.SECONDS);

			final Filter adapterFilter = new Filter();
			adapterFilter.name = "CsvSourceAdapter";
			adapterFilter.arguments = new HashMap<>();
			adapterFilter.arguments.put("csvFormat", "DEFAULT");

			final Filter dbFilter = new Filter();
			dbFilter.name = "DbInsertionFilter";

			filterChain.filters.add(adapterFilter);
			filterChain.filters.add(dbFilter);

			filterClient.add(sourceId, filterId, filterChain);
		}

		{
			// check filter execution
			Thread.sleep(2000);
			ArrayNode data = dataClient.getAll(sourceId);
			Assert.assertTrue(data.size() > 0);
		}

		{
			// cleanup
			sourceClient.remove(sourceId);
		}
	}

}
