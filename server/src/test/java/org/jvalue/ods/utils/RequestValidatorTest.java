package org.jvalue.ods.utils;

import org.junit.Test;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;

public class RequestValidatorTest {


	@Test
	public void testValidateSuccess() throws IOException {

		String dataSourceDescription =
			"{\n" +
				"\t\"data\":{\n" +
				"\t\t\"id\":\"osm\",\n" +
				"\t\t\"type\":\"DataSource\",\n" +
				"\t\t\"attributes\":{\n" +
				"\t\t\t\"domainIdKey\":\"/id\",\n" +
				"\t\t\t\"schema\":{},\n" +
				"\t\t\t\"metaData\":{\n" +
				"\t\t\t\t\"name\":\"OSM\",\n" +
				"\t\t\t\t\"title\":\"OSM\",\n" +
				"\t\t\t\t\"authorEmail\":\"\",\n" +
				"\t\t\t\t\"notes\":\"\",\n" +
				"\t\t\t\t\"author\":\"\",\n" +
				"\t\t\t\t\"termsOfUse\":\"\",\n" +
				"\t\t\t\t\"url\":\"\"\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}";

		JsonApiRequest request = JsonMapper.readValue(dataSourceDescription, JsonApiRequest.class);

		DataSourceDescription sourceDescription = JsonMapper.convertValue(
			request.getAttributes(),
			DataSourceDescription.class);

		RequestValidator.validate(sourceDescription);
	}

	@Test(expected = WebApplicationException.class)
	public void testValidateFail() throws IOException {

		String dataSourceDescription =
			"{\n" +
				"\t\"data\":{\n" +
				"\t\t\"id\":\"osm\",\n" +
				"\t\t\"type\":\"DataSource\",\n" +
				"\t\t\"attributes\":{\n" +
				"\t\t\t\"domainIdKey\":\"/id\",\n" +
				"\t\t\t\"schema\":{},\n" +
				"\t\t\t\"metaData\":{\n" +
				"\t\t\t\t\"name\":\"OSM\",\n" +
				"\t\t\t\t\"title\":\"OSM\",\n" +
				"\t\t\t\t\"notes\":\"\",\n" +
				"\t\t\t\t\"author\":\"\",\n" +
				"\t\t\t\t\"termsOfUse\":\"\",\n" +
				"\t\t\t\t\"url\":\"\"\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}";

		JsonApiRequest request = JsonMapper.readValue(dataSourceDescription, JsonApiRequest.class);

		DataSourceDescription sourceDescription = JsonMapper.convertValue(
			request.getAttributes(),
			DataSourceDescription.class);

		RequestValidator.validate(sourceDescription);
	}
}
