package org.jvalue.ods.api.sources;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.AbstractApiTest;

import java.util.List;

import retrofit.RetrofitError;

public final class DataSourceApiTest extends AbstractApiTest {

	@Test
	public void testCrud() {
		// test add and get
		Assert.assertEquals(dataSource, dataSourceApi.get(sourceId));
		Assert.assertEquals(sourceId, dataSource.getId());
		Assert.assertEquals(domainIdKey, dataSource.getDomainIdKey());
		Assert.assertEquals(schema, dataSource.getSchema());
		Assert.assertEquals(metaData, dataSource.getMetaData());

		// test get all
		List<DataSource> sources = dataSourceApi.getAll();
		Assert.assertTrue(sources.size() > 0);
		Assert.assertTrue(sources.contains(dataSource));

		// test remove
		dataSourceApi.remove(sourceId);
		try {
			dataSourceApi.get(sourceId);
		} catch(RetrofitError re) {
			dataSourceApi.add(sourceId, dataSourceDescription);
			return;
		}
		Assert.fail();
	}

}
