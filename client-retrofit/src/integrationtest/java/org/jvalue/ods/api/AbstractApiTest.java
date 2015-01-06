package org.jvalue.ods.api;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Before;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceApi;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public abstract class AbstractApiTest {

	protected final DataSourceApi dataSourceApi;

	protected final JsonPointer domainIdKey = JsonPointer.compile("/someId");
	protected final JsonNode schema = new ObjectNode(JsonNodeFactory.instance);
	protected final DataSourceMetaData metaData = new DataSourceMetaData("", "", "", "", "", "", "");
	protected final String sourceId = getClass().getSimpleName();
	protected final DataSourceDescription dataSourceDescription = new DataSourceDescription(domainIdKey, schema, metaData);

	protected DataSource dataSource;


	protected AbstractApiTest() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter())
				.setEndpoint("http://localhost:8080/ods/api/v1")
				.build();

		this.dataSourceApi = restAdapter.create(DataSourceApi.class);
	}


	@Before
	public void addSource() {
		this.dataSource = dataSourceApi.add(sourceId, dataSourceDescription);
	}


	@After
	public void removeSource() {
		dataSourceApi.remove(sourceId);
	}

}
