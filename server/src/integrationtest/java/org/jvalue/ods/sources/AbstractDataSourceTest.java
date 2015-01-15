package org.jvalue.ods.sources;


import com.fasterxml.jackson.databind.node.ArrayNode;

import org.junit.Assert;
import org.jvalue.ods.ApiFactory;
import org.jvalue.ods.api.data.DataApi;
import org.jvalue.ods.api.processors.ProcessorApi;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSourceApi;
import org.jvalue.ods.api.sources.DataSourceDescription;

public abstract class AbstractDataSourceTest {

	private final ApiFactory apiFactory = new ApiFactory();
	private final DataSourceApi sourceApi = apiFactory.createDataSourceApi();
	private final ProcessorApi processorApi = apiFactory.createProcessorApi();
	private final DataApi dataApi = apiFactory.createDataApi();


	protected final void runTest(
			final DataSourceDescription source,
			final ProcessorReferenceChainDescription processorChain,
			final long sleepDuration) throws Exception {

		final String sourceId = getClass().getSimpleName();
		final String filterId = "testFilter";

		// add source
		sourceApi.add(sourceId, source);

		// assert empty
		ArrayNode data = dataApi.getAll(sourceId);
		Assert.assertEquals(0, data.size());

		// add filter chain
		processorApi.add(sourceId, filterId, processorChain);

		// check filter execution
		Thread.sleep(sleepDuration);
		data = dataApi.getAll(sourceId);
		Assert.assertTrue(data.size() > 0);

		// cleanup
		sourceApi.remove(sourceId);
	}

}
