/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;

import java.util.List;


@RunWith(JMockit.class)
public final class JsonSourceAdapterTest extends AbstractSourceAdapterTest {

	@Mocked private MetricRegistry registry;
	@Mocked private DataSource source;

	private static final ArrayNode JSON_ARRAY;
	private static final ObjectNode JSON_OBJECT;
	static {
		JSON_ARRAY = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = JSON_ARRAY.addObject();
		jsonObject.put("key1", "value1");
		jsonObject.put("key2", "value2");
		jsonObject = JSON_ARRAY.addObject();
		jsonObject.put("key3", "value1");
		jsonObject.put("key4", "value2");
	}

	static {
		JSON_OBJECT = new ObjectNode(JsonNodeFactory.instance);
		JSON_OBJECT.put("key5","value11");
		JSON_OBJECT.put("key6","value42");
		ObjectNode jsonObject = JSON_OBJECT.putObject("person");
		jsonObject.put("name","myName");

	}


	@Test
	public void testBasicAdapterObject(@Mocked DataSource source) throws Exception {
		List<ObjectNode> jsonResult = testAdapterWithAllProtocols(JSON_OBJECT.toString());

		Assert.assertEquals(1, jsonResult.size());
		Assert.assertEquals(JSON_OBJECT, jsonResult.get(0));
	}


	@Test
	public void testBasicAdapter(@Mocked DataSource source) throws Exception {
		List<ObjectNode> jsonResult = testAdapterWithAllProtocols(JSON_ARRAY.toString());
		for (int i = 0; i < jsonResult.size(); ++i) {
			Assert.assertEquals(JSON_ARRAY.get(i), jsonResult.get(i));
		}
		Assert.assertEquals(JSON_ARRAY.size(), jsonResult.size());
	}


	@Override
	protected SourceAdapter createAdapter(String sourceUrl) {
		return new JsonSourceAdapter(source, sourceUrl, registry);
	}

}
