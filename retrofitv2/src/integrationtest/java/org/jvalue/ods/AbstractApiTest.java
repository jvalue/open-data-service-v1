package org.jvalue.ods;

import api.DataSourceApi;
import api.NotificationApi;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.jvalue.commons.utils.HttpServiceCheck;
import org.jvalue.commons.utils.Log;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiDocument;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.DataSourceWrapper;
import org.jvalue.ods.utils.JsonMapper;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

import java.io.IOException;

public abstract class AbstractApiTest {

	private static final String ODS_API_URL = "http://localhost:8080/ods/api";

	protected DataSourceApi dataSourceApi;
	protected NotificationApi notificationApi;

	protected final JsonPointer domainIdKey = JsonPointer.compile("/someId");
	protected final JsonNode schema = new ObjectNode(JsonNodeFactory.instance);
	protected final DataSourceMetaData metaData = new DataSourceMetaData("", "", "", "", "", "", "");
	protected final String sourceId = getClass().getSimpleName();
	protected final DataSource dataSource = new DataSource(sourceId, domainIdKey, schema, metaData);

	protected DataSource dataSourceResponse;

	@Before
	public void setUp() {
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
	}

	@Before
	public void addSource() throws IOException {
//		JsonApiDocument document = dataSourceApi.getAllSources();

		DataSourceWrapper addRequest = DataSourceWrapper.from(dataSource);
		JsonApiRequest request = JsonApiRequest.from(addRequest);
		System.out.println(JsonMapper.writeValueAsString(addRequest));
		System.out.println(JsonMapper.writeValueAsString(request));
		JsonApiDocument doc = dataSourceApi.addSource(request);
		dataSourceResponse = getEntityFromDoc(doc, DataSource.class, 0);
		System.out.println(dataSourceResponse.toString());
	}


	protected static <T> T getEntityFromDoc(JsonApiDocument doc, Class<T> clazz, int index) {
		return JsonMapper.convertValue(doc.getData().get(index).getEntity(), clazz);
	}


}
