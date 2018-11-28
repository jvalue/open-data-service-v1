package org.jvalue.ods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
		DataSource result = convertResponseToDataSource(response);

		Assert.assertTrue(response.getData().isObject());
		Assert.assertEquals(dataSource, result);
	}


	@Test
	public void testGetAllDataSources() {
		ResponseBody response = dataSourceApi.getAllSources();
		List<DataSource> results = convertResponseToDataSourceList(response);

		Assert.assertTrue(response.getData().isArray());
		Assert.assertTrue(results.contains(dataSource));
	}


	@Test
	public void testGetSchema() {
		ResponseBody response = dataSourceApi.getSourceSchema(sourceId);
		DataSource result = convertResponseToDataSource(response);

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


	private DataSource convertResponseToDataSource(ResponseBody response) {
		addIdFieldToAttributes(response);
		return response.dataToTargetObject(DataSource.class);
	}


	private List<DataSource> convertResponseToDataSourceList(ResponseBody response) {
		addIdFieldToAttributes(response);
		return response.dataToTargetObjectList(DataSource.class);
	}


	private void addIdFieldToAttributes(ResponseBody response) {
		if(response.getData().isArray()) {
			for (JsonNode node : response.getData()) {
				((ObjectNode) node.get("attributes")).put("id", node.get("id").asText());
			}
		} else {
			((ObjectNode) response.getData().get("attributes")).put("id", response.getId());
		}
	}
}
