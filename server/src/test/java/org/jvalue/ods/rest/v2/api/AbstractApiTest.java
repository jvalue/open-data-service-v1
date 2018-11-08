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

		assertHasValidErrorObj(result);
		assertExcDocHasValues(result, COMBINED_MESSAGE, ERRCODE_VALID);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testCreateJsonApiExceptionInvalidCode() {
		AbstractApi.createJsonApiException(MESSAGE, ERRCODE_INVALID);
	}
}
