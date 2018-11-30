package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.utils.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@RunWith(JMockit.class)
public class MultiSourceAdapterTest {

	@Mocked
	private MetricRegistry registry;

	@Mocked
	private DataSource dataSource;

	@Mocked
	private JsonSourceAdapter jsonSourceAdapter;

	@Mocked
	private SourceAdapterFactory sourceAdapterFactory;

	private static final ArrayNode JSON_ARRAY_1;
	private static final ArrayNode JSON_ARRAY_2;

	static {
		JSON_ARRAY_1 = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = JSON_ARRAY_1.addObject();
		jsonObject.put("array1key1", "value1");
		jsonObject.put("array1key2", "value2");
		jsonObject = JSON_ARRAY_1.addObject();
		jsonObject.put("array1key3", "value3");
		jsonObject.put("array1key4", "value4");
	}


	static {
		JSON_ARRAY_2 = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = JSON_ARRAY_2.addObject();
		jsonObject.put("array2key1", "value1");
		jsonObject.put("array2key2", "value2");
		jsonObject = JSON_ARRAY_2.addObject();
		jsonObject.put("array2key3", "value3");
		jsonObject.put("array2key4", "value4");
	}


	@Test
	public void testWithOneSource() throws IOException {
		ArrayList<LinkedHashMap<String, Object>> adapters = createAdapterList(1);

		new Expectations() {{
			jsonSourceAdapter.iterator(); result = JSON_ARRAY_1.iterator();
			sourceAdapterFactory.createJsonSourceAdapter(dataSource, anyString); result = jsonSourceAdapter;
		}};

		SourceAdapter multiSourceAdapter = new MultiSourceAdapter(sourceAdapterFactory, dataSource, adapters, registry);
		ObjectNode result = multiSourceAdapter.iterator().next();

		Assert.assertFalse(result.get("sourceNr1").isNull());
		Assert.assertEquals(JSON_ARRAY_1, result.get("sourceNr1"));
	}


	@Test
	public void testWithTwoSources() throws IOException {
		ArrayList<LinkedHashMap<String, Object>> adapters = createAdapterList(2);

		new Expectations() {{
			jsonSourceAdapter.iterator(); times = 2; returns(JSON_ARRAY_1.iterator(), JSON_ARRAY_2.iterator());
			sourceAdapterFactory.createJsonSourceAdapter(dataSource, anyString); times= 2; result = jsonSourceAdapter;
		}};

		SourceAdapter multiSourceAdapter = new MultiSourceAdapter(sourceAdapterFactory, dataSource, adapters, registry);

		ObjectNode result = multiSourceAdapter.iterator().next();

		Assert.assertFalse(result.get("sourceNr1").isNull());
		Assert.assertFalse(result.get("sourceNr2").isNull());
		Assert.assertEquals(JSON_ARRAY_1, result.get("sourceNr1"));
		Assert.assertEquals(JSON_ARRAY_2, result.get("sourceNr2"));
	}


	private ArrayList<LinkedHashMap<String, Object>> createAdapterList(int size) throws IOException {
		ArrayList<LinkedHashMap<String, Object>> adapters = new ArrayList<>();

		for (int i = 1; i <= size; i++) {
			adapters.add(createAdapter("sourceNr"+ i));
		}

		return adapters;
	}


	private LinkedHashMap<String, Object> createAdapter(String alias) throws IOException {
		String adapterStr = "{\"source\":{\"name\":\"JsonSourceAdapter\",\"arguments\":{\"sourceUrl\":\"https://example.og\"}},\"alias\":\""+ alias +"\"}";
		return JsonMapper.getInstance().readValue(adapterStr, new TypeReference<LinkedHashMap<String, Object>>(){});
	}

}
