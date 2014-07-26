package org.jvalue.ods.server.utils;

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ods.utils.Assert;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class RestletResult {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String
		KEY_STATUS = "status",
		KEY_MESSAGE = "message";


	public static RestletResult newSuccessResult() {
		return newSuccessResult(Status.SUCCESS_NO_CONTENT, null);
	}


	public static RestletResult newSuccessResult(JsonNode data) {
		Assert.assertNotNull(data);
		return newSuccessResult(Status.SUCCESS_OK, data);
	}


	public static RestletResult newSuccessResult(Status status, JsonNode data) {
		Assert.assertNotNull(status);
		Assert.assertTrue(status.isSuccess(), "not an success status");

		return new RestletResult(status, data);
	}


	public static RestletResult newErrorResult(Status status, String errorMsg) {
		Assert.assertNotNull(status, errorMsg);
		Assert.assertTrue(status.isError(), "not an error status");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(KEY_STATUS, status.getCode());
		data.put(KEY_MESSAGE, errorMsg);
		JsonNode json = mapper.valueToTree(data);

		return new RestletResult(status, json);
	}


	private final JsonNode data;
	private final Status status;

	private RestletResult(Status status, JsonNode data) {
		Assert.assertNotNull(status);
		this.status = status;
		this.data = data;
	}


	public Status getStatus() {
		return status;
	}


	public JsonNode getData() {
		return data;
	}

}
