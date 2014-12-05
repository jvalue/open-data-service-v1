package org.jvalue.ods.rest;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.filter.reference.FilterChainExecutionInterval;
import org.jvalue.ods.rest.client.FilterChainClient;
import org.jvalue.ods.rest.model.Filter;
import org.jvalue.ods.rest.model.FilterChain;

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
			List<FilterChain> references = filterClient.getAll(sourceId);
			Assert.assertEquals(0, references.size());
		}

		{
			// add and get
			final Filter adapter = new Filter();
			adapter.name = "JsonSourceAdapter";

			final FilterChain reference = new FilterChain();
			reference.executionInterval = new FilterChainExecutionInterval(100, TimeUnit.MINUTES);
			reference.filters = new LinkedList<>();
			reference.filters.add(adapter);

			filterClient.add(sourceId, filterId, reference);
			FilterChain receivedReference = filterClient.get(sourceId, filterId);
			Assert.assertEquals(filterId, receivedReference.id);
			Assert.assertEquals(reference.executionInterval, receivedReference.executionInterval);
			Assert.assertEquals(reference.filters.size(), receivedReference.filters.size());
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
