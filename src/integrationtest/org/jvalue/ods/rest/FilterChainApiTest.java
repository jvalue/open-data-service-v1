package org.jvalue.ods.rest;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.processor.reference.ExecutionInterval;
import org.jvalue.ods.rest.client.FilterChainClient;
import org.jvalue.ods.rest.model.Processor;
import org.jvalue.ods.rest.model.ProcessorChainReference;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;

public final class FilterChainApiTest extends AbstractApiTest {

	private static final FilterChainClient filterClient = clientFactory.getFilterChainClient();


	@Test
	public void testCrud() throws Exception {
		final String filterId = FilterChainApiTest.class.getSimpleName();
		{
			// check empty
			List<ProcessorChainReference> references = filterClient.getAll(sourceId);
			Assert.assertEquals(0, references.size());
		}

		{
			// add and get
			final Processor adapter = new Processor();
			adapter.name = "JsonSourceAdapter";
			adapter.arguments.put("sourceUrl", "http://localhost:8080");

			final ProcessorChainReference reference = new ProcessorChainReference();
			reference.executionInterval = new ExecutionInterval(100, TimeUnit.MINUTES);
			reference.processors = new LinkedList<>();
			reference.processors.add(adapter);

			filterClient.add(sourceId, filterId, reference);
			ProcessorChainReference receivedReference = filterClient.get(sourceId, filterId);
			Assert.assertEquals(filterId, receivedReference.id);
			Assert.assertEquals(reference.executionInterval, receivedReference.executionInterval);
			Assert.assertEquals(reference.processors.size(), receivedReference.processors.size());
		}

		{
			// delete filter chain
			filterClient.remove(sourceId, filterId);
			try {
				filterClient.get(sourceId, filterId);
				Assert.fail("filter chain not removed but should be");
			} catch (RetrofitError re) {
				Assert.assertEquals(404, re.getResponse().getStatus());
			}
		}
	}

}
