package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.jvalue.ods.data.DataSource;


public final class JsonSourceAdapterTest extends AbstractSourceAdapterTest {

	private static final ArrayNode JSON_ARRAY;
	static {
		JSON_ARRAY = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = new ObjectNode(JsonNodeFactory.instance);
		jsonObject.put("key1", "value1");
		jsonObject.put("key2", "value2");
		JSON_ARRAY.add(jsonObject);
	}


	@Override
	protected SourceAdapter getSourceAdapter(DataSource source) {
		return new JsonSourceAdapter(source);
	}


	@Override
	protected String getContent() {
		return JSON_ARRAY.toString();
	}


	@Override
	protected void assertEqualsContent(ArrayNode jsonArray) {
		Assert.assertEquals(JSON_ARRAY, jsonArray);
	}

}
