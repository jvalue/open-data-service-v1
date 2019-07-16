/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;


public class CustomInstantDeserializer extends StdDeserializer<Instant> {

    public CustomInstantDeserializer() {
        this(null);
    }

    public CustomInstantDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Instant deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return Instant.parse(node.asText());
    }
}
