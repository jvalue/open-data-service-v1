package org.jvalue.ods.sources;


import com.fasterxml.jackson.databind.node.ArrayNode;

import org.junit.Assert;
import org.jvalue.ods.rest.client.ClientFactory;
import org.jvalue.ods.rest.client.DataClient;
import org.jvalue.ods.rest.client.DataSourceClient;
import org.jvalue.ods.rest.client.FilterChainClient;
import org.jvalue.ods.rest.model.DataSource;
import org.jvalue.ods.rest.model.FilterChain;

public abstract class AbstractDataSourceTest {

	private final ClientFactory clientFactory = new ClientFactory();
	private final DataSourceClient sourceClient = clientFactory.getDataSourceClient();
	private final FilterChainClient filterClient = clientFactory.getFilterChainClient();
	private final DataClient dataClient = clientFactory.getDataClient();


	protected final void runTest(
			final DataSource source,
			final FilterChain filterChain,
			final long sleepDuration) throws Exception {

		final String sourceId = getClass().getSimpleName();
		final String filterId = "testFilter";

		// add source
		sourceClient.add(sourceId, source);

		// assert empty
		ArrayNode data = dataClient.getAll(sourceId);
		Assert.assertEquals(0, data.size());

		// add filter chain
		filterClient.add(sourceId, filterId, filterChain);

		// check filter execution
		Thread.sleep(sleepDuration);
		data = dataClient.getAll(sourceId);
		Assert.assertTrue(data.size() > 0);

		// cleanup
		sourceClient.remove(sourceId);
	}



}
