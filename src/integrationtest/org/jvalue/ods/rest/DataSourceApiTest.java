package org.jvalue.ods.rest;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.rest.retrofit.ClientFactory;
import org.jvalue.ods.rest.retrofit.DataSource;
import org.jvalue.ods.rest.retrofit.DataSourceClient;

import java.net.URL;
import java.util.List;

import retrofit.RetrofitError;

public final class DataSourceApiTest {

	@Test
	public void testCrud() throws Exception {
		DataSourceClient client = new ClientFactory().getDataSourceClient();

		String sourceId = DataSourceApiTest.class.getSimpleName();

		DataSource sentSource = new DataSource();
		sentSource.url = new URL("http://localhost:8080/");
		sentSource.metaData = new DataSourceMetaData("", "", "", "", "", "", "");
		sentSource.domainIdKey = "/someId";
		sentSource.schema = new ObjectNode(JsonNodeFactory.instance);

		{
			// add and get source
			client.add(sourceId, sentSource);
			DataSource receivedSource = client.get(sourceId);
			assertEquals(sentSource, receivedSource);
			Assert.assertEquals(sourceId, receivedSource.id);
		}

		{
			// get all sources
			List<DataSource> sources = client.getAll();
			DataSource source = null;
			for (DataSource s : sources) {
				if (sourceId.equals(s.id)) {
					source = s;
					break;
				}
			}
			Assert.assertNotNull("failed to find source in getAll", source);
			assertEquals(sentSource, source);
		}

		{
			// delete source
			client.remove(sourceId);
			try {
				client.get(sourceId);
				Assert.fail("source not removed but should be");
			} catch (RetrofitError re) {
				Assert.assertEquals(404, re.getResponse().getStatus());
			}
		}
	}

	private void assertEquals(DataSource source1, DataSource source2) {
		Assert.assertEquals(source1.url, source2.url);
		Assert.assertEquals(source1.metaData, source2.metaData);
		Assert.assertEquals(source1.domainIdKey, source2.domainIdKey);
		Assert.assertEquals(source1.schema, source2.schema);
	}

}
