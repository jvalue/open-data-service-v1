package org.jvalue.ods.api;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Before;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public abstract class AbstractApiTest {

	protected DataSourceApi dataSourceApi;
	protected NotificationApi notificationApi;

	protected final JsonPointer domainIdKey = JsonPointer.compile("/someId");
	protected final JsonNode schema = new ObjectNode(JsonNodeFactory.instance);
	protected final DataSourceMetaData metaData = new DataSourceMetaData("", "", "", "", "", "", "");
	protected final String sourceId = getClass().getSimpleName();
	protected final DataSourceDescription dataSourceDescription = new DataSourceDescription(domainIdKey, schema, metaData);

	protected DataSource dataSource;


	protected AbstractApiTest() {
	}


	@Before
	public void setup() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter())
				.setEndpoint("http://localhost:8080/ods/api")
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						request.addHeader("Accept", "application/json");
						request.addHeader("Content-Type", "application/json");
						request.addHeader("Authorization", "Basic YWRtaW5AYWRtaW5sYW5kLmNvbTphZG1pbjEyMw=="); // admin@adminland.org : admin123
					}
				})
				.build();

		dataSourceApi = restAdapter.create(DataSourceApi.class);
		notificationApi = restAdapter.create(NotificationApi.class);
	}


	@Before
	public void addSource() {
		this.dataSource = dataSourceApi.addSourceSynchronously(sourceId, dataSourceDescription);
	}


	@After
	public void removeSource() {
		dataSourceApi.deleteSourceSynchronously(sourceId);
	}

}
