package org.jvalue.ods;

import api.DataSourceApi;
import api.NotificationApi;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jsonapi.ResponseBody;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.jvalue.commons.utils.HttpServiceCheck;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.rest.v2.jsonapi.request.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataSourceWrapper;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

import java.io.IOException;

public abstract class AbstractApiTest {

	private static final String ODS_API_URL = "http://localhost:8080/ods/api";

	protected static DataSourceApi dataSourceApi;
	protected static NotificationApi notificationApi;

	protected static final JsonPointer domainIdKey = JsonPointer.compile("/someId");
	protected static final JsonNode schema = new ObjectNode(JsonNodeFactory.instance);
	protected static final DataSourceMetaData metaData = new DataSourceMetaData("", "", "", "", "", "", "");
	protected static final String sourceId = DataSourceApiTest.class.getSimpleName() + "V2";
	protected static final DataSource dataSource = new DataSource(sourceId, domainIdKey, schema, metaData);
	protected static ResponseBody responseDataSource;


	@BeforeClass
	public static void setUp() throws IOException {
		HttpServiceCheck.check(ODS_API_URL + "/v2");
		RestAdapter restAdapter = new RestAdapter.Builder()
			.setConverter(new JacksonConverter())
			.setEndpoint(ODS_API_URL)
			.setRequestInterceptor(
				request -> {
					request.addHeader("Accept", JsonApiResponse.JSONAPI_TYPE);
					request.addHeader("Content-Type", JsonApiResponse.JSONAPI_TYPE);
					request.addHeader("Authorization", "Basic YWRtaW5AYWRtaW5sYW5kLmNvbTphZG1pbjEyMw=="); // admin@adminland.org : admin123
				})
			.build();

		dataSourceApi = restAdapter.create(DataSourceApi.class);
		notificationApi = restAdapter.create(NotificationApi.class);

		addSource();
	}


	protected static void addSource() throws IOException {
		DataSourceWrapper addRequest = DataSourceWrapper.from(dataSource);
		JsonApiRequest request = JsonApiRequest.from(addRequest);

		responseDataSource = dataSourceApi.addSource(request);
	}


	@AfterClass
	public static void removeSource() throws IOException{
		dataSourceApi.deleteSource(sourceId);
	}


}
