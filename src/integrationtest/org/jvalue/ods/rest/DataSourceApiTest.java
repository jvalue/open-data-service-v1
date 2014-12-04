package org.jvalue.ods.rest;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.model.DataSource;

import java.util.List;

import retrofit.RetrofitError;

public final class DataSourceApiTest extends AbstractDataSourceTest {

	@Test
	public void testCrud() throws Exception {
		{
			// get source
			DataSource receivedSource = sourceClient.get(sourceId);
			assertEquals(source, receivedSource);
			Assert.assertEquals(sourceId, receivedSource.id);
		}

		{
			// get all sources
			List<DataSource> sources = sourceClient.getAll();
			DataSource receivedSource = null;
			for (DataSource s : sources) {
				if (sourceId.equals(s.id)) {
					receivedSource = s;
					break;
				}
			}
			Assert.assertNotNull("failed to find source in getAll", receivedSource);
			assertEquals(source, receivedSource);
		}

		{
			// delete source
			removeDataSource();
			try {
				sourceClient.get(sourceId);
				Assert.fail("source not removed but should be");
			} catch (RetrofitError re) {
				Assert.assertEquals(404, re.getResponse().getStatus());
			}
			addDataSource(); // otherwise afterClass method will be in a bad mood
		}
	}


	private void assertEquals(DataSource source1, DataSource source2) {
		Assert.assertEquals(source1.url, source2.url);
		Assert.assertEquals(source1.metaData, source2.metaData);
		Assert.assertEquals(source1.domainIdKey, source2.domainIdKey);
		Assert.assertEquals(source1.schema, source2.schema);
	}

}
