package org.jvalue.ods;

import jsonapi.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.sources.DataSource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DataSourceApiTest extends AbstractApiTest {


	@Test
	public void testCreateDataSources() {
		DataSource result = convertResponseToDataSource(responseDataSource);

		//check
	}


	@Test
	public void testGetSingleDataSource() {
		ResponseBody response = dataSourceApi.getSource(sourceId);
		DataSource result = convertResponseToDataSource(response);

		Assert.assertEquals(dataSource, result);
	}


	@Test
	public void testGetAllDataSources() {
		throw new NotImplementedException();
	}


	@Test
	public void testGetSchema() {
		throw new NotImplementedException();
	}


	@Test
	public void testDeleteDataSource() {
		throw new NotImplementedException();
	}


	private DataSource convertResponseToDataSource(ResponseBody response) {
		DataSource result = response.dataToTargetObject(DataSource.class);

		return new DataSource(response.getId(), result.getDomainIdKey(), result.getSchema(), result.getMetaData());
	}
}
