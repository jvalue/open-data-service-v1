package org.jvalue.ods.sources;


import org.junit.Assert;
import org.jvalue.ods.ApiFactory;
import org.jvalue.ods.api.DataApi;
import org.jvalue.ods.api.DataSourceApi;
import org.jvalue.ods.api.ProcessorChainApi;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSourceDescription;

import java.util.List;

public abstract class AbstractDataSourceTest {

	private final ApiFactory apiFactory = new ApiFactory();
	private final DataSourceApi sourceApi = apiFactory.createDataSourceApi();
	private final ProcessorChainApi processorApi = apiFactory.createProcessorChainApi();
	private final DataApi dataApi = apiFactory.createDataApi();


	protected final void runTest(
			final DataSourceDescription source,
			final ProcessorReferenceChainDescription processorChain,
			final long sleepDuration) throws Exception {

		final String sourceId = getClass().getSimpleName();
		final String filterId = "testFilter";

		// add source
		sourceApi.addSource(sourceId, source);

		// assert empty
		List<Object> data = dataApi.getAllObjects(sourceId);
		Assert.assertEquals(0, data.size());

		// add filter chain
		processorApi.addProcessorChain(sourceId, filterId, processorChain);

		// check filter execution
		Thread.sleep(sleepDuration);
		data = dataApi.getAllObjects(sourceId);
		Assert.assertTrue(data.size() > 0);

		// cleanup
		sourceApi.deleteSource(sourceId);
	}

}
