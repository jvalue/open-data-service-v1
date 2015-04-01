package org.jvalue.ods.api;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.sources.DataSource;

import java.util.List;

import retrofit.RetrofitError;

public final class DataSourceApiTest extends AbstractApiTest {

	@Test
	public void testCrud() {
		// test add and get
		Assert.assertEquals(dataSource, dataSourceApi.getSourceSynchronously(sourceId));
		Assert.assertEquals(sourceId, dataSource.getId());
		Assert.assertEquals(domainIdKey, dataSource.getDomainIdKey());
		Assert.assertEquals(schema, dataSource.getSchema());
		Assert.assertEquals(metaData, dataSource.getMetaData());

		// test get schema
		Assert.assertEquals(schema, dataSourceApi.getSourceSchemaSynchronously(sourceId));

		// test get all
		List<DataSource> sources = dataSourceApi.getAllSourcesSynchronously();
		Assert.assertTrue(sources.size() > 0);
		Assert.assertTrue(sources.contains(dataSource));

		// test remove
		dataSourceApi.deleteSourceSynchronously(sourceId);
		try {
			dataSourceApi.getSourceSchemaSynchronously(sourceId);
		} catch(RetrofitError re) {
			dataSourceApi.addSourceSynchronously(sourceId, dataSourceDescription);
			return;
		}
		Assert.fail("source was not removed");
	}

}
