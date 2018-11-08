package org.jvalue.ods.rest.v2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import org.junit.Assert;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiDocument;

import javax.ws.rs.core.Response;

public class TestUtils {

	public static final int NR_ENTITIES = 10;
	public static ObjectMapper objectMapper = new ObjectMapper();


	public static JsonNode extractJsonEntity(Response from) {
		return objectMapper.valueToTree(from.getEntity());
	}


	public static int countFields(JsonNode node) {
		return Iterators.size(node.fieldNames());
	}


	public static void assertIsValidJsonApiDataResponse(Response response) {
		Assert.assertTrue(response.getEntity() instanceof JsonApiDocument);
		JsonApiDocument jDoc = (JsonApiDocument) response.getEntity();
		Assert.assertTrue(jDoc.getData() != null);
	}


	public static void assertHasValidData(JsonNode jsonApiDocument) {
		Assert.assertTrue(jsonApiDocument.has("data"));
		assertIsValidDataNode(jsonApiDocument.get("data"));

		assertSelfLinkExist(jsonApiDocument);
	}


	public static void assertHasValidRelationshipData(JsonNode jsonApiDocument) {
		Assert.assertTrue(jsonApiDocument.has("data"));
		assertIsValidDataNode(jsonApiDocument.get("data"));

		assertRelatedLinkExists(jsonApiDocument);
	}


	public static void assertIsValidDataNode(JsonNode dataNode) {
		if (dataNode.isArray()) {
			for (int i = 0; i < NR_ENTITIES; i++) {
				assertIsValidDataNode(dataNode.get(i));
			}
		} else {
			Assert.assertTrue(dataNode.has("id"));
			Assert.assertTrue(dataNode.has("type"));
		}
	}


	public static void assertRelatedLinkExists(JsonNode document) {
		String expectedUrl = "scheme://authority/path/to/testCollection/entity";
		Assert.assertEquals(expectedUrl, document.get("links").get("related").textValue());
	}


	public static void assertSelfLinkExist(JsonNode document) {
		String expectedUrl = "scheme://authority/path/to/testCollection/entity";
		Assert.assertEquals(expectedUrl, document.get("links").get("self").textValue());
	}

}
