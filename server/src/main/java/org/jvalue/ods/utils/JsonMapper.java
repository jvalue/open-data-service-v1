package org.jvalue.ods.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonMapper {

	private static ObjectMapper objectMapper;

	private JsonMapper() {}

	public static ObjectMapper getInstance() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		}

		return objectMapper;
	}


	public static String writeValueAsString(Object value) throws JsonProcessingException {
		return getInstance().writeValueAsString(value);
	}


	public static  <T> T readValue(String content, Class<T> valueType) throws IOException {
		return getInstance().readValue(content, valueType);
	}
}
