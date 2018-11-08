package org.jvalue.ods.rest.v2.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.v2.TestUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.jvalue.ods.rest.v2.TestUtils.*;

public class AbstractApiTest {

	@Test
	public void testCreateJsonApiException() {
		WebApplicationException exception = AbstractApi.createJsonApiException(MESSAGE, ERRCODE_VALID);

		JsonNode result = TestUtils.extractJsonEntity(exception.getResponse());

		Assert.assertTrue(result.has("errors"));
		Assert.assertTrue(result.get("errors").isArray());
		Assert.assertEquals(COMBINED_MESSAGE, result.get("errors").get(0).get("message").asText());
		Assert.assertEquals(ERRCODE_VALID, result.get("errors").get(0).get("code").asInt());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testCreateJsonApiExceptionInvalidCode() {
		AbstractApi.createJsonApiException(MESSAGE, ERRCODE_INVALID);
	}
}
