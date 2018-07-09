package org.jvalue.ods.rest.v2.jsonApi;

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
import java.util.List;

import static org.jvalue.ods.rest.v2.jsonApi.TestEntityProvider.*;

@RunWith(JMockit.class)
public class JsonApiResponseTest{

    @Mocked private UriInfo uriInfo;
    private static ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testResponseOkSingleObject() {
        //Record
        new Expectations() {{
            uriInfo.getAbsolutePath(); result = URI.create(ENTITY_PATH);
        }};
        TestEntity minimalEntity = new TestEntityProvider.MinimalEntity();


        //Replay
        Response ok = new JsonApiResponse()
                .uriInfo(uriInfo)
                .ok()
                .entity(minimalEntity)
                .build();


        //Verify
        assertIsValidJsonApiDataResponse(ok);
        JsonNode jEntity = createJson(ok.getEntity());
        Assert.assertEquals(TEST_ID, jEntity.get("data").get("id").textValue());
        Assert.assertEquals(TEST_COLLECTION, jEntity.get("data").get("type").textValue());
        Assert.assertEquals(ENTITY_PATH, jEntity.get("links").get("self").textValue());
        Assert.assertTrue(jEntity.get("data").has("attributes"));
    }


    @Test
    public void testResponseOkCollection() {

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
        Response collectionOk = new JsonApiResponse()
                .uriInfo(uriInfo)
                .ok()
                .entity(entityList)
                .build();

        //Verify
        assertIsValidJsonApiDataResponse(collectionOk);
        JsonNode jArray = createJson(collectionOk.getEntity());
        Assert.assertTrue(jArray.get("data").isArray());
        //todo: add more verification (at least id & type for each collection element)
    }


    @Test(timeout = 5000)
    public void testResponseCreated() {
        //Record
        new Expectations() {{
            uriInfo.getAbsolutePath(); result = URI.create(ENTITY_PATH);
        }};
        TestEntity testEntity = new TestEntityProvider.EntityWithAttributes();

        //Replay
        Response create = new JsonApiResponse()
                .uriInfo(uriInfo)
                .created()
                .entityIdentifier(testEntity)
                .build();

        //Verify
        assertIsValidJsonApiDataResponse(create);
        JsonNode jEntity = createJson(create.getEntity());
        Assert.assertEquals(2, countFields(jEntity.get("data")));
    }


    private static JsonNode createJson(Object from) {
        return objectMapper.valueToTree(from);
    }


    private int countFields(JsonNode node) {
        return Iterators.size(node.fieldNames());
    }

    private static void assertIsValidJsonApiDataResponse(Response response) {
        Assert.assertTrue(response.getEntity() instanceof JsonApiDocument);
        JsonApiDocument jDoc = (JsonApiDocument) response.getEntity();
        Assert.assertTrue(jDoc.getData() instanceof JsonApiData);
    }

    //todo: add assertions for single data and collection data to ensure existence of id and type attributes

}
