package org.jvalue.ods.rest;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.ApiFactory;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorApi;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.*;
import org.jvalue.ods.api.sources.DataSourceApi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;

public final class ProcessorChainApiTest {

	private final ApiFactory apiFactory = new ApiFactory();
	private final DataSourceApi sourceApi = apiFactory.createDataSourceApi();
	private final ProcessorApi processorApi = apiFactory.createProcessorApi();

	private final String sourceId = getClass().getSimpleName();

	@Before
	public void setupSource() {
		DataSourceDescription description = new DataSourceDescription(
				JsonPointer.compile("/id"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));

		sourceApi.add(sourceId, description);
	}


	@After
	public void teardownSource() {
		sourceApi.remove(sourceId);
	}


	@Test
	public void testProcessorReferenceChainValidation() {
		List<ProcessorReference> processors = Arrays.asList(
				new ProcessorReference("JsonSourceAdapter", new HashMap<String, Object>()),
				new ProcessorReference("DbInsertionFilter", new HashMap<String, Object>()));
		ProcessorReferenceChainDescription description = new ProcessorReferenceChainDescription(
				processors,
				new ExecutionInterval(100, TimeUnit.SECONDS));

		try {
			processorApi.add(sourceId, "someFilterId", description);
			Assert.fail("ODS accepted invalid processor chain");
		} catch (RetrofitError re) {
			// all good
		}
	}

}
