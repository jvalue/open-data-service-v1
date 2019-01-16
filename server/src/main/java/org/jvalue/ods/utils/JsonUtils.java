/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.util.LinkedList;
import java.util.List;

public class JsonUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private JsonUtils() {
	}


	public static JsonNode createRestrictedEntity(JsonApiIdentifiable entity, List<String> properties) {

		ObjectNode originalNode = objectMapper.valueToTree(entity);
		ObjectNode restrictedNode = objectMapper.createObjectNode();

		properties.forEach(propertyName -> restrictedNode.set(propertyName, originalNode.get(propertyName)));

		return restrictedNode;
	}


	public static JsonNode createRestrictedEntity(JsonApiIdentifiable entity, String property) {
		List<String> properties = new LinkedList<>();
		properties.add(property);
		return createRestrictedEntity(entity, properties);
	}

}
