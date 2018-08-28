package org.jvalue.ods.rest.v2.jsonapi.response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
	public void testEquals() {
		Map<String, Object> attributes = new LinkedHashMap<>();
		attributes.put("name", "Rick");
		attributes.put("dimension", "C-132");
		JsonApiRequest result = new JsonApiRequest("myType","id42",attributes);

		Assert.assertEquals(request, result);
	}

}
