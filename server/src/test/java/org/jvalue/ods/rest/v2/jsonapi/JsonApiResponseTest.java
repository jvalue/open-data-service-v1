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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.jvalue.ods.rest.v2.jsonapi.TestEntityProvider.*;

@RunWith(JMockit.class)
public class JsonApiResponseTest{

    @Mocked private UriInfo uriInfo;
    private static ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testGetResponse() {
        //Record
        new Expectations() {{
            uriInfo.getAbsolutePath(); result = URI.create(ENTITY_PATH);
        }};
        TestEntity minimalEntity = new TestEntityProvider.MinimalEntity();


        //Replay
        Response ok = JsonApiResponse
                .createGetResponse(uriInfo)
                .data(minimalEntity)
                .build();


        //Verify
        assertIsValidJsonApiDataResponse(ok);
        JsonNode jEntity = extractJsonEntity(ok);
        Assert.assertEquals(TEST_ID, jEntity.get("data").get("id").textValue());
        Assert.assertEquals("MinimalEntity", jEntity.get("data").get("type").textValue());
        Assert.assertEquals(ENTITY_PATH, jEntity.get("links").get("self").textValue());
        Assert.assertTrue(jEntity.get("data").has("attributes"));
    }


    @Test
    public void testGetResponseCollection() {

        final int NR_ENTITIES = 10;

        //Record
        new Expectations() {{
            uriInfo.getAbsolutePath(); result = URI.create(COLLECTION_PATH);
        }};
        List<TestEntity> entityList = new ArrayList<>();
        for(int i = 0; i < NR_ENTITIES; i++) {
            entityList.add(new CollectableEntity(String.valueOf(i)));
        }

        //Replay
        Response collectionOk = JsonApiResponse
                .createGetResponse(uriInfo)
                .data(entityList)
                .build();

        //Verify
        assertIsValidJsonApiDataResponse(collectionOk);
        JsonNode jArray = extractJsonEntity(collectionOk);
        Assert.assertTrue(jArray.get("data").isArray());

    }


    public void testPostResponse() {
        //Record
        new Expectations() {{
            uriInfo.getAbsolutePath(); result = URI.create(ENTITY_PATH);
        }};
        TestEntity testEntity = new EntityWithAttributes();

        //Replay
        Response create = JsonApiResponse
                .createPostResponse(uriInfo)
                .data(testEntity)
                .build();

        //Verify
        assertIsValidJsonApiDataResponse(create);;
        assertHasValidData(extractJsonEntity(create));
    }

    @Test
    public void testToIdentifier() {
        //Record
        new Expectations() {{
            uriInfo.getAbsolutePath(); result = URI.create(ENTITY_PATH);
        }};
        TestEntity testEntity = new EntityWithAttributes();

        //Replay
        Response ok = JsonApiResponse
                .createGetResponse(uriInfo)
                .data(testEntity)
                .toIdentifier()
                .build();

        //Verify
        assertIsValidJsonApiDataResponse(ok);
        JsonNode identifierNode = extractJsonEntity(ok);
        Assert.assertEquals(2, countFields(identifierNode));
        assertHasValidData(identifierNode);
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
        Assert.assertTrue(jDoc.getData() instanceof JsonApiData || jDoc.getData() instanceof Collection);
    }

    private static void assertHasValidData(JsonNode jsonApiDocument) {
        Assert.assertTrue(jsonApiDocument.has("data"));
        Assert.assertTrue(jsonApiDocument.get("data").has("id"));
        Assert.assertTrue(jsonApiDocument.get("data").has("type"));
    }

}
