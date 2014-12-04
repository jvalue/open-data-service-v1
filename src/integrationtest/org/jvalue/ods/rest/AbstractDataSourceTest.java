package org.jvalue.ods.rest;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.rest.client.ClientFactory;
import org.jvalue.ods.rest.model.DataSource;
import org.jvalue.ods.rest.client.DataSourceClient;

import java.net.URL;
import java.util.UUID;


public abstract class AbstractDataSourceTest {

	protected static final ClientFactory clientFactory = new ClientFactory();
	protected static final DataSourceClient sourceClient = clientFactory.getDataSourceClient();

	protected static final DataSource source = new DataSource();
	protected static String sourceId = "TEST_" + UUID.randomUUID().toString();

	static {
		try {
			source.url = new URL("http://localhost:8080/");
			source.metaData = new DataSourceMetaData("", "", "", "", "", "", "");
			source.domainIdKey = "/someId";
			source.schema = new ObjectNode(JsonNodeFactory.instance);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@BeforeClass
	public static void addDataSource() {
		sourceClient.add(sourceId, source);
	}


	@AfterClass
	public static void removeDataSource() {
		sourceClient.remove(sourceId);
	}

}
