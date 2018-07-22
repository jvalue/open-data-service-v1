package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;
import org.jvalue.ods.rest.v2.jsonapi.document.JsonApiDocument;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.jvalue.ods.rest.v2.jsonapi.response.TestEntityProvider.*;

@RunWith(JMockit.class)
public class JsonApiResponseTest {

	@Mocked
	private UriInfo uriInfo;
	private static ObjectMapper objectMapper = new ObjectMapper();


	@Before
	public void setUp() throws Exception {
		//Record
		new Expectations() {{
			uriInfo.getAbsolutePath();
			result = URI.create(ENTITY_PATH);
		}};
	}

	@Test
	public void testGetResponse() {
		//Record
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
			entityList.add(createCustomMinimalEntity(String.valueOf(i)));
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
	public void testGetResponseRestrictTo() {
		//Record
		JsonApiIdentifiable entity = createEntityWithAttributes();

		//Replay
		Response result = JsonApiResponse
			.createGetResponse(uriInfo)
			.data(entity)
			.restrictTo("intAttribute")
			.build();

		//Verify
		assertIsValidJsonApiDataResponse(result);
		JsonNode resultJson = extractJsonEntity(result).get("data");
		System.out.println(resultJson);
		Assert.assertEquals(3, resultJson.size());
		Assert.assertTrue(resultJson.has("attributes"));
		Assert.assertEquals(1, resultJson.get("attributes").size());
		Assert.assertEquals(1, resultJson.get("attributes").get("intAttribute").asInt());
	}


	@Test
	public void testPostResponse() {
		//Record
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
	public void testAddLinks() {
		//Record
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


	@Test
	public void testAddRelationship() {
		//Record
		JsonApiIdentifiable minimalEntity = createMinimalEntity();
		JsonApiIdentifiable relatedEntity = createCustomMinimalEntity("related");

		//Replay
		Response result = JsonApiResponse
			.createGetResponse(uriInfo)
			.data(minimalEntity)
			.addRelationship("related", relatedEntity, uriInfo.getAbsolutePath())
			.build();

		assertIsValidJsonApiDataResponse(result);
		JsonNode resultJson = extractJsonEntity(result).get("data");
		Assert.assertTrue(resultJson.has("relationships"));
		Assert.assertEquals(1, resultJson.get("relationships").size());
		Assert.assertTrue(resultJson.get("relationships").has("related"));
		JsonNode relationshipNode = resultJson.get("relationships").get("related");
		assertHasValidRelationshipData(relationshipNode);
		Assert.assertEquals("related", relationshipNode.get("data").get("id").textValue());
	}


	@Test
	public void testAddIncluded() {
		//Record
		JsonApiIdentifiable minimalEntity = createMinimalEntity();
		JsonApiIdentifiable relatedEntity = createCustomMinimalEntity("related");

		//Replay
		Response result = JsonApiResponse
			.createGetResponse(uriInfo)
			.data(minimalEntity)
			.addRelationship("related", relatedEntity, uriInfo.getAbsolutePath())
			.addIncluded(relatedEntity, uriInfo.getAbsolutePath())
			.build();

		//Verify
		assertIsValidJsonApiDataResponse(result);
		JsonNode resultJson = extractJsonEntity(result);
		Assert.assertTrue(resultJson.has("included"));
		Assert.assertEquals(1, resultJson.get("included").size());
		JsonNode includedData = resultJson.get("included").get(0);
		assertIsValidDataNode(includedData);
		Assert.assertEquals("related", includedData.get("id").textValue());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddIncludedWithDifferentIdThrowsIllegalArgumentException() {
		//Record
		JsonApiIdentifiable minimalEntity = createMinimalEntity();
		JsonApiIdentifiable relatedEntity = createCustomMinimalEntity("related");
		JsonApiIdentifiable unrleatedEntity = createCustomMinimalEntity("unrelated");

		//Replay
		JsonApiResponse
			.createGetResponse(uriInfo)
			.data(minimalEntity)
			.addRelationship("related", relatedEntity, uriInfo.getAbsolutePath())
			.addIncluded(unrleatedEntity, uriInfo.getAbsolutePath())
			.build();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAddIncludedWithDifferentTypeThrowsIllegalArgumentException() {
		//Record
		JsonApiIdentifiable minimalEntity = createMinimalEntity();
		JsonApiIdentifiable relatedEntity = createCustomMinimalEntity(TEST_ID);
		JsonApiIdentifiable unrelatedEntity = createEntityWithAttributes();

		//Replay
		JsonApiResponse
			.createGetResponse(uriInfo)
			.data(minimalEntity)
			.addRelationship("related", relatedEntity, uriInfo.getAbsolutePath())
			.addIncluded(unrelatedEntity, uriInfo.getAbsolutePath())
			.build();
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
		assertIsValidDataNode(jsonApiDocument.get("data"));

		assertSelfLinkExist(jsonApiDocument);
	}


	private static void assertHasValidRelationshipData(JsonNode jsonApiDocument) {
		Assert.assertTrue(jsonApiDocument.has("data"));
		assertIsValidDataNode(jsonApiDocument.get("data"));

		assertRelatedLinkExists(jsonApiDocument);
	}


	private static void assertIsValidDataNode(JsonNode dataNode) {
		Assert.assertTrue(dataNode.has("id"));
		Assert.assertTrue(dataNode.has("type"));
	}


	private static void assertRelatedLinkExists(JsonNode document) {
		String expectedUrl = "scheme://authority/path/to/testCollection/entity";
		Assert.assertEquals(expectedUrl, document.get("links").get("related").textValue());
	}


	private static void assertSelfLinkExist(JsonNode document) {
		String expectedUrl = "scheme://authority/path/to/testCollection/entity";
		Assert.assertEquals(expectedUrl, document.get("links").get("self").textValue());
	}

}
