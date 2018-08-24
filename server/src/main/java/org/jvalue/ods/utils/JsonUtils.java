package org.jvalue.ods.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiDocument;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() { }


    public static JsonNode createRestrictedEntity (JsonApiIdentifiable entity, List<String> properties) {

        ObjectNode originalNode = objectMapper.valueToTree(entity);
        ObjectNode restrictedNode = objectMapper.createObjectNode();

        properties.forEach(propertyName -> restrictedNode.set(propertyName, originalNode.get(propertyName)));

        return restrictedNode;
    }


    public static JsonNode createRestrictedEntity (JsonApiIdentifiable entity, String property) {
        List<String> properties = new LinkedList<>();
        properties.add(property);
        return createRestrictedEntity(entity, properties);
    }


    public static JsonNode createJsonNode(Object from) {
    	Assert.assertNotNull(from);

    	return objectMapper.valueToTree(from);
	}


	/**
	 * Get a Map of all attributes of the entity which is contained in the given JsonApiDocument
	 * @param doc the document which contains the entity
	 * @return a map containing all attributes
	 * @throws IOException
	 */
	public static Map<String, Object> getAttributesMap(JsonApiDocument doc) throws IOException {
		Assert.assertNotNull(doc);
		Assert.assertEquals(1, doc.getData().size(), "JsonApiDocument needs to contain exactly one entity.");

		JsonNode entityNode = objectMapper.valueToTree(doc.getData().get(0).getEntity());

		Map<String, Object> attributeMap = getMapFromJson(entityNode.get("attributes"));

		attributeMap.put("id", entityNode.get("id"));
		attributeMap.put("type", entityNode.get("type"));

		return attributeMap;
	}


	/**
	 * Converts a given JsonNode to a map
	 * @param node the node to convert
	 * @return a map containing each attribute of the node as entry
	 */
    public static Map<String, Object> getMapFromJson(JsonNode node) throws IOException {
    	return objectMapper.readValue(
    		node.toString(),
			new TypeReference<Map<String, Object>>(){});
	}


	public static void assertIsValidJsonApiSingleDataDocument(JsonNode jsonApiDocument) {
    	Assert.assertNotNull(jsonApiDocument);
    	Assert.assertTrue( jsonApiDocument.has("data") );
    	JsonNode dataNode = jsonApiDocument.get("data");
		Assert.assertFalse( dataNode.isArray(), "document contains resource array but single resource object is expected" );
		Assert.assertTrue(dataNode.has("id"));
		Assert.assertTrue(dataNode.has("type"));
    }


	/**
	 * get an attribute from a given JsonNode or an empty JsonNode if the attribute is not present
	 * @param from the node to fetch from
	 * @param attribute the attribute to get
	 * @return the attribute as JsonNode or an empty JsonNode
	 */
    public static JsonNode getIfPresent(JsonNode from, String attribute) {
    	if(from.has(attribute)) {
    		return from.get(attribute);
		} else {
    		return objectMapper.createObjectNode();
		}
	}
}
