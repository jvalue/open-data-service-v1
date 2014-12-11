package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.adapter.server.FtpServer;
import org.jvalue.ods.filter.adapter.server.HttpServer;
import org.jvalue.ods.filter.adapter.server.Server;

import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class JsonSourceAdapterTest {

	private static final ArrayNode JSON_ARRAY;
	static {
		JSON_ARRAY = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = new ObjectNode(JsonNodeFactory.instance);
		jsonObject.put("key1", "value1");
		jsonObject.put("key2", "value2");
		JSON_ARRAY.add(jsonObject);
	}


	@Test
	public void testJsonAdapter(@Mocked final DataSource source) throws Exception {
		List<Server> serverList = new LinkedList<>();
		serverList.add(new FtpServer());
		serverList.add(new HttpServer());

		for (final Server server : serverList) {
			new Expectations() {{
				source.getUrl();
				result = server.getFileUrl();
			}};

			server.start(JSON_ARRAY.toString());

			JsonSourceAdapter adapter = new JsonSourceAdapter(source);
			ArrayNode resultJsonArray = adapter.grabSource();
			Assert.assertEquals(JSON_ARRAY, resultJsonArray);

			server.stop();
		}

	}

}
