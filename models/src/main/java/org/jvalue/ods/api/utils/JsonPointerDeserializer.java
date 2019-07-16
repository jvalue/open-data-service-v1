/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.utils;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public final class JsonPointerDeserializer extends JsonDeserializer<JsonPointer> {

	@Override
	public JsonPointer deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
		return JsonPointer.compile(jsonParser.getText());
	}

}
