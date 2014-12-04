package org.jvalue.ods.rest;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.client.DataViewClient;
import org.jvalue.ods.rest.model.DataView;

import java.util.List;

import retrofit.RetrofitError;

public final class DataViewApiTest extends AbstractDataSourceTest {

	private static final DataViewClient viewClient = clientFactory.getDataViewClient();


	@Test
	public void testCrud() throws Exception {
		final String viewId = DataViewApiTest.class.getSimpleName();
		{
			// check empty
			List<DataView> views = viewClient.getAll(sourceId);
			Assert.assertEquals(0, views.size());
		}

		{
			// add and get
			DataView view = new DataView();
			view.mapFunction = "function(doc) { emit(null, doc) }";

			viewClient.add(sourceId, viewId, view);
			DataView receivedView = viewClient.get(sourceId, viewId);
			Assert.assertEquals(viewId, receivedView.id);
			Assert.assertEquals(view.mapFunction, receivedView.mapFunction);
			Assert.assertEquals(view.reduceFunction, receivedView.reduceFunction);
		}

		{
			// execute
			viewClient.execute(sourceId, viewId);
			viewClient.execute(sourceId, viewId, "someDummyValue");
		}

		{
			// delete
			viewClient.remove(sourceId, viewId);
			try {
				viewClient.get(sourceId, viewId);
				Assert.fail("data view not removed but should be");
			} catch (RetrofitError re) {
				Assert.assertEquals(404, re.getResponse().getStatus());
			}
		}
	}

}
