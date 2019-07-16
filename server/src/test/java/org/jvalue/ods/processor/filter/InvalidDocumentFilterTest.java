/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.*;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.sources.DataSource;

public final class InvalidDocumentFilterTest {

	@Mocked private MetricRegistry registry;
	@Mocked private DataSource source;

	@Test
	public void testRemoval() {
		ObjectNode jsonObject = new ObjectNode(JsonNodeFactory.instance);
		jsonObject.put("key", "value");
		jsonObject.put("hello", "world");

		ArrayNode jsonArray = new ArrayNode(JsonNodeFactory.instance);
		jsonArray.add(new TextNode("Hello there"));
		jsonArray.add(new IntNode(42));
		jsonArray.add(jsonObject);

		InvalidDocumentFilter filter = new InvalidDocumentFilter(source, registry);
		ArrayNode jsonResult = filter.doFilter(jsonArray);

		Assert.assertEquals(1, jsonResult.size());
		Assert.assertEquals(jsonResult.get(0), jsonObject);
	}

}
