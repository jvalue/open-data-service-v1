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

	public static final String MESSAGE = "test_message";
	public static final int ERRCODE_VALID = 400;
	public static final int ERRCODE_INVALID = 200;
	public static final String VALID_ERRMSG =
		Response.Status.fromStatusCode(ERRCODE_VALID).getReasonPhrase();
	public static final String COMBINED_MESSAGE =
			VALID_ERRMSG
			+ ": "
			+ MESSAGE;


	public static JsonNode extractJsonEntity(Response from) {
		return objectMapper.valueToTree(from.getEntity());
	}


	public static int countFields(JsonNode node) {
		return Iterators.size(node.fieldNames());
	}


	public static void assertIsValidJsonApiErrorResponse(Response response) {
		Assert.assertTrue(response.getEntity() instanceof JsonApiDocument);
		JsonApiDocument jDoc = (JsonApiDocument) response.getEntity();
		Assert.assertTrue(jDoc.getErrors().size() > 0);
		assertHasValidErrorObj(extractJsonEntity(response));
	}


	public static void assertHasValidErrorObj(JsonNode jsonApiDocument) {
		Assert.assertFalse(jsonApiDocument.has("data"));
		Assert.assertTrue(jsonApiDocument.has("errors"));
		Assert.assertTrue(jsonApiDocument.get("errors").get(0).has("message"));
		Assert.assertTrue(jsonApiDocument.get("errors").get(0).has("code"));
	}


	public static void assertExceptionResponseHasValues(Response response, String message, int code) {
		JsonNode jDoc = extractJsonEntity(response);
		assertExcDocHasValues(jDoc, message, code);
	}

	public static void assertExcDocHasValues(JsonNode jDoc, String message, int code) {
		Assert.assertEquals(message, jDoc.get("errors").get(0).get("message").asText());
		Assert.assertEquals(code, jDoc.get("errors").get(0).get("code").asInt());
	}


	public static void assertIsValidJsonApiDataResponse(Response response) {
		Assert.assertTrue(response.getEntity() instanceof JsonApiDocument);
		JsonApiDocument jDoc = (JsonApiDocument) response.getEntity();
		Assert.assertTrue(jDoc.getData() != null);
	}


	public static void assertHasValidData(JsonNode jsonApiDocument) {
		Assert.assertFalse(jsonApiDocument.has("errors"));
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
