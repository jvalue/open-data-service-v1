package org.jvalue.ods.rest;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.filter.reference.FilterChainExecutionInterval;
import org.jvalue.ods.rest.client.FilterChainClient;
import org.jvalue.ods.rest.model.FilterChainReference;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;

public final class FilterChainApiTest extends AbstractDataSourceTest {

	private static final FilterChainClient filterClient = clientFactory.getFilterChainClient();


	@Test
	public void testCrud() throws Exception {
		final String filterId = FilterChainApiTest.class.getSimpleName();
		{
			// check empty
			List<FilterChainReference> references = filterClient.getAll(sourceId);
			Assert.assertEquals(0, references.size());
		}

		{
			// add and get
			FilterChainReference reference = new FilterChainReference();
			reference.executionInterval = new FilterChainExecutionInterval(100, TimeUnit.MINUTES);
			reference.filterNames = new LinkedList<>();
			reference.filterNames.add("JsonSourceAdapter");

			filterClient.add(sourceId, filterId, reference);
			FilterChainReference receivedReference = filterClient.get(sourceId, filterId);
			Assert.assertEquals(filterId, receivedReference.id);
			Assert.assertEquals(reference.executionInterval, receivedReference.executionInterval);
			Assert.assertEquals(reference.filterNames.size(), receivedReference.filterReferences.size());
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
