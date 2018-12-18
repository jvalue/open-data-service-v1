package org.jvalue.ods;

import jsonapi.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.sources.DataSource;
import retrofit.RetrofitError;

import java.io.IOException;
import java.util.List;

public class DataSourceApiTest extends AbstractApiTest {


	@Test
	public void testCreateDataSources() {
		Assert.assertEquals("DataSourceApiTestV2", responseDataSource.getId());
		Assert.assertEquals("DataSource", responseDataSource.getType());

		Assert.assertFalse(responseDataSource.getData().isNull());
		Assert.assertFalse(responseDataSource.getLinks().isNull());
		Assert.assertTrue(responseDataSource.getError().isNull());
	}


	@Test
	public void testGetSingleDataSource() {
		ResponseBody response = dataSourceApi.getSource(sourceId);
		DataSource result = response.dataToTargetObject(DataSource.class);

		Assert.assertTrue(response.getData().isObject());
		Assert.assertEquals(dataSource, result);
	}


	@Test
	public void testGetAllDataSources() {
		ResponseBody response = dataSourceApi.getAllSources();
		List<DataSource> results = response.dataToTargetObjectList(DataSource.class);

		Assert.assertTrue(results.contains(dataSource));
	}


	@Test
	public void testGetSchema() {
		ResponseBody response = dataSourceApi.getSourceSchema(sourceId);
		DataSource result = response.dataToTargetObject(DataSource.class);

		Assert.assertEquals(schema, result.getSchema());
	}


	@Test
	public void testDeleteDataSource() throws IOException {
		dataSourceApi.deleteSource(sourceId);

		try {
			dataSourceApi.getSource(sourceId);
		} catch(RetrofitError re) {
			addSource();
			return;
		}

		Assert.fail("data source was not removed");
	}

}
