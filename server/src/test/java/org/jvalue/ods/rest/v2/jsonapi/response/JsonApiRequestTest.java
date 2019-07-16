/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.rest.v2.jsonapi.request.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;
import org.jvalue.ods.utils.JsonMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonApiRequestTest {

	private static JsonApiRequest request;

	@Before
	public void setup() {
		Map<String, Object> attributes = new LinkedHashMap<>();
		attributes.put("name", "Rick");
		attributes.put("dimension", "C-132");
		request = new JsonApiRequest("myType","id42",attributes);
	}


	@Test
	public void testDeserialization() throws IOException {
		String reqStr = "{\"data\":{\"type\":\"myType\",\"id\":\"id42\",\"attributes\":{\"name\":\"Rick\",\"dimension\":\"C-132\"}}}";

		JsonApiRequest result = JsonMapper.readValue(reqStr, JsonApiRequest.class);

		Assert.assertEquals(request, result);
	}


	@Test
	public void testFromIdentifiable() throws IOException {
		JsonApiRequest result = JsonApiRequest.from(new Dummy("42"));

		Assert.assertEquals("42", result.getId());
		Assert.assertEquals("Dummy", result.getType());
		Assert.assertEquals(3, result.getAttributes().size());
		Assert.assertTrue(result.getAttributes().containsKey("customField"));
	}


	@Test
	public void testEquals() {
		Map<String, Object> attributes = new LinkedHashMap<>();
		attributes.put("name", "Rick");
		attributes.put("dimension", "C-132");
		JsonApiRequest result = new JsonApiRequest("myType","id42",attributes);

		Assert.assertEquals(request, result);
	}


	private class Dummy implements JsonApiIdentifiable {

		private String id;
		private String customField = "someValue";

		public Dummy (String id ) {
			this.id = id;
		}

		public String getCustomField() {
			return customField;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getType() {
			return "Dummy";
		}
	}

}
