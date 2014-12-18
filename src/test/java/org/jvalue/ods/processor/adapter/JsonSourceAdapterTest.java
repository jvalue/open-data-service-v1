package org.jvalue.ods.processor.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;

import java.util.List;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class JsonSourceAdapterTest extends AbstractSourceAdapterTest {

	private static final ArrayNode JSON_ARRAY;
	static {
		JSON_ARRAY = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = new ObjectNode(JsonNodeFactory.instance);
		jsonObject.put("key1", "value1");
		jsonObject.put("key2", "value2");
		JSON_ARRAY.add(jsonObject);
	}


	@Test
	public void testBasicAdapter(@Mocked DataSource source) throws Exception {
		AbstractSourceAdapter adapter = new JsonSourceAdapter(source);
		List<ObjectNode> jsonResult = testAdapterWithAllProtocols(source, adapter, JSON_ARRAY.toString());
		for (int i = 0; i < jsonResult.size(); ++i) {
			Assert.assertEquals(JSON_ARRAY.get(i), jsonResult.get(i));
		}
	}

}
