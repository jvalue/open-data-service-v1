package org.jvalue.ods.rest.v2.jsonapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;
import org.jvalue.ods.rest.v2.jsonapi.document.JsonApiDocument;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.jvalue.ods.rest.v2.jsonapi.TestEntityProvider.*;

@RunWith(JMockit.class)
public class JsonApiResponseTest {

	@Mocked
	private UriInfo uriInfo;
	private static ObjectMapper objectMapper = new ObjectMapper();


	@Test
	public void testGetResponse() {
		//Record
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create(ENTITY_PATH);
		}};
		JsonApiIdentifiable minimalEntity = createMinimalEntity();


		//Replay
		Response result = JsonApiResponse
				.createGetResponse(uriInfo)
				.data(minimalEntity)
				.build();


		//Verify
		Assert.assertEquals(200, result.getStatus());
		assertIsValidJsonApiDataResponse(result);
		JsonNode jEntity = extractJsonEntity(result);
		Assert.assertEquals(TEST_ID, jEntity.get("data").get("id").textValue());
		Assert.assertEquals(ENTITY_PATH, jEntity.get("links").get("self").textValue());
		Assert.assertTrue(jEntity.get("data").has("attributes"));
	}


	@Test
	public void testGetResponseCollection() {

		final int NR_ENTITIES = 10;

		//Record
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create(COLLECTION_PATH);
		}};
		List<JsonApiIdentifiable> entityList = new ArrayList<>();
		for (int i = 0; i < NR_ENTITIES; i++) {
			entityList.add(createCollectableEntity(i));
		}

		//Replay
		Response result = JsonApiResponse
				.createGetResponse(uriInfo)
				.data(entityList)
				.build();

		//Verify
		Assert.assertEquals(200, result.getStatus());
		assertIsValidJsonApiDataResponse(result);
		JsonNode jArray = extractJsonEntity(result);
		Assert.assertTrue(jArray.get("data").isArray());

	}

	@Test
	public void testPostResponse() {
		//Record
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create(ENTITY_PATH);
		}};
		JsonApiIdentifiable testEntity = createEntityWithAttributes();

		//Replay
		Response result = JsonApiResponse
				.createPostResponse(uriInfo)
				.data(testEntity)
				.build();

		//Verify
		Assert.assertEquals(201, result.getStatus());
		assertIsValidJsonApiDataResponse(result);
		assertHasValidData(extractJsonEntity(result));
	}

	@Test
	public void testPutResponse() {
		//Record
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create(ENTITY_PATH);
		}};
		JsonApiIdentifiable testEntity = createEntityWithAttributes();

		//Replay
		Response result = JsonApiResponse
				.createPutResponse(uriInfo)
				.data(testEntity)
				.build();

		//Verify
		Assert.assertEquals(200, result.getStatus());
		assertIsValidJsonApiDataResponse(result);
		assertHasValidData(extractJsonEntity(result));
	}

	@Test
	public void testToIdentifier() {
		//Record
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create(ENTITY_PATH);
		}};
		JsonApiIdentifiable testEntity = createEntityWithAttributes();

		//Replay
		Response result = JsonApiResponse
				.createGetResponse(uriInfo)
				.data(testEntity)
				.toIdentifier()
				.build();

		//Verify
		assertIsValidJsonApiDataResponse(result);
		JsonNode identifierNode = extractJsonEntity(result);
		Assert.assertEquals(2, countFields(identifierNode));
		assertHasValidData(identifierNode);
	}

	@Test
	public void testAddLinks() {
		//Record
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create(ENTITY_PATH);
		}};
		JsonApiIdentifiable minimalEntity = createMinimalEntity();
		String linkUrl = "http://test.de";

		//Replay
		Response result = JsonApiResponse
				.createGetResponse(uriInfo)
				.data(minimalEntity)
				.addLink("TestLink", URI.create(linkUrl))
				.build();

		JsonNode resultAsJson = extractJsonEntity(result);
		Assert.assertTrue(resultAsJson.get("links").has("TestLink"));
		Assert.assertEquals(linkUrl, resultAsJson.get("links").get("TestLink").asText());
	}


	/*
	TEST UTILS
	 */
	private static JsonNode extractJsonEntity(Response from) {
		return objectMapper.valueToTree(from.getEntity());
	}


	private static int countFields(JsonNode node) {
		return Iterators.size(node.fieldNames());
	}


	private static void assertIsValidJsonApiDataResponse(Response response) {
		Assert.assertTrue(response.getEntity() instanceof JsonApiDocument);
		JsonApiDocument jDoc = (JsonApiDocument) response.getEntity();
		Assert.assertTrue(jDoc.getData() != null);
	}


	private static void assertHasValidData(JsonNode jsonApiDocument) {
		Assert.assertTrue(jsonApiDocument.has("data"));
		Assert.assertTrue(jsonApiDocument.get("data").has("id"));
		Assert.assertTrue(jsonApiDocument.get("data").has("type"));

		assertSelfLinkExist(jsonApiDocument);
	}


	private static void assertSelfLinkExist(JsonNode document) {
		String expectedUrl = "scheme://authority/path/to/testCollection/entity";
		Assert.assertEquals(expectedUrl, document.get("links").get("self").textValue());
	}

}
