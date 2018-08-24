package org.jvalue.ods.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


	/**
	 * Converts a given JsonNode to a map
	 *
	 * @param node the node to convert
	 * @return a map containing each attribute of the node as entry
	 */
	public static Map<String, Object> getMapFromJson(JsonNode node) throws IOException {
		return objectMapper.readValue(
			node.toString(),
			new TypeReference<Map<String, Object>>() {
			});
	}
}
