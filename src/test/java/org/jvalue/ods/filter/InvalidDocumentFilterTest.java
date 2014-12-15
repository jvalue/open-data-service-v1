package org.jvalue.ods.filter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Test;

public final class InvalidDocumentFilterTest {

	@Test
	public void testRemoval() {
		ObjectNode jsonObject = new ObjectNode(JsonNodeFactory.instance);
		jsonObject.put("key", "value");
		jsonObject.put("hello", "world");

		ArrayNode jsonArray = new ArrayNode(JsonNodeFactory.instance);
		jsonArray.add(new TextNode("Hello there"));
		jsonArray.add(new IntNode(42));
		jsonArray.add(jsonObject);

		InvalidDocumentFilter filter = new InvalidDocumentFilter();
		ArrayNode jsonResult = filter.doProcess(jsonArray);

		Assert.assertEquals(1, jsonResult.size());
		Assert.assertEquals(jsonResult.get(0), jsonObject);
	}

}
