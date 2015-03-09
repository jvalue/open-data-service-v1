package org.jvalue.ods.sources;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.ApiFactory;
import org.jvalue.ods.api.DataApi;
import org.jvalue.ods.api.DataSourceApi;
import org.jvalue.ods.api.ProcessorChainApi;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSourceDescription;

public abstract class AbstractDataSourceTest {

	private final ApiFactory apiFactory = new ApiFactory();
	private final DataSourceApi sourceApi = apiFactory.createDataSourceApi();
	private final ProcessorChainApi processorApi = apiFactory.createProcessorChainApi();
	private final DataApi dataApi = apiFactory.createDataApi();

	private String sourceId = getClass().getSimpleName();


	@Before
	public void setupSourceAndFilter() {
		// add source
		sourceApi.addSource(sourceId, getSourceDescription());

		// assert empty
		Data data = dataApi.getObjects(sourceId, getStartId(), 3, null);
		Assert.assertEquals(0, data.getCursor().getCount());
		Assert.assertEquals(0, data.getResult().size());

		// add filter chain
		processorApi.addProcessorChain(sourceId, "testFilter", getProcessorChainDescription());
	}


	@After
	public void removeSource() {
		sourceApi.deleteSource(sourceId);
	}


	@Test
	public final void runTest() throws Exception {
		// check filter execution
		Thread.sleep(getSleepDuration());
		Data data = dataApi.getObjects(sourceId, getStartId(), 3, null);
		Assert.assertTrue(data.getCursor().getCount() > 0);
		Assert.assertTrue(data.getResult().size() > 0);
	}


	public abstract DataSourceDescription getSourceDescription();

	public abstract String getStartId();

	public abstract ProcessorReferenceChainDescription getProcessorChainDescription();

	public abstract long getSleepDuration();

}
