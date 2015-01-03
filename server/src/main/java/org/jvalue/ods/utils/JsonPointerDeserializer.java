package org.jvalue.ods.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

public final class JsonPointerDeserializer extends JsonDeserializer<JsonPointer> {

	@Override
	public JsonPointer deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		try {
			return JsonPointer.compile(jsonParser.getText());
		} catch (Exception e) {
			throw new JsonMappingException(e.getMessage());
		}
	}

}
