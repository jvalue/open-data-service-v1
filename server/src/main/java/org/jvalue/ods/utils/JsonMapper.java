package org.jvalue.ods.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jvalue.commons.rest.RestUtils;

import java.io.IOException;
import java.util.Map;

public class JsonMapper {

	private static ObjectMapper objectMapper;

	private JsonMapper() {
	}

	public static ObjectMapper getInstance() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		}

		return objectMapper;
	}


	public static String writeValueAsString(Object value) throws JsonProcessingException {
		return getInstance().writeValueAsString(value);
	}


	public static <T> T readValue(String content, Class<T> valueType) throws IOException {
		return getInstance().readValue(content, valueType);
	}


	public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
		T result;
		try {
			result = getInstance().convertValue(fromValue, toValueType);
		} catch (IllegalArgumentException e) {
			throw RestUtils.createJsonFormattedException("Malformed " + toValueType.getSimpleName() + ": " + e.getMessage(), 400);
		}

		return result;
	}


	public static Map<String, Object> convertValueToMap(Object fromValue) {
		return getInstance().convertValue(fromValue, new TypeReference<Map<String,Object>>(){});
	}


	public static <T extends JsonNode> T valueToTree(Object fromValue) {
		return getInstance().valueToTree(fromValue);
	}

}
