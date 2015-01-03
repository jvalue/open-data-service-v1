package org.jvalue.ods.utils;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public final class JsonPointerSerializer extends JsonSerializer<JsonPointer> {

	@Override
	public void serialize(JsonPointer jsonPointer, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		jsonGenerator.writeString(jsonPointer.toString());
	}

}
