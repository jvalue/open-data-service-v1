package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;

import java.io.ByteArrayInputStream;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class CsvSourceAdapterTest {

	private static final String CSV = "key1,key2\nvalue1,value2\nvalue3,value4";

	@Test
	public void testAdapter(
			@Mocked final DataSource source) throws Exception {

		new Expectations() {{
			source.getUrl().openConnection().getInputStream();
			result = new ByteArrayInputStream(CSV.getBytes());
		}};

		CsvSourceAdapter adapter =new CsvSourceAdapter(source);
		ArrayNode array = adapter.grabSource();

		Assert.assertEquals(2, array.size());

		ObjectNode node = (ObjectNode) array.get(0);
		Assert.assertEquals("value1", node.get("key1").asText());
		Assert.assertEquals("value2", node.get("key2").asText());

		node = (ObjectNode) array.get(1);
		Assert.assertEquals("value3", node.get("key1").asText());
		Assert.assertEquals("value4", node.get("key2").asText());
	}

}
