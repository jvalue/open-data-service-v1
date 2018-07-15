package org.jvalue.ods.rest.v2.jsonapi.document;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;

public class JsonApiResourceTest {

    private final Dummy dummyObj = new Dummy("id_01");
    private final URI uri = URI.create("http://test.com");


    @Test
    public void testGetEntity() {
        JsonApiResource result = new JsonApiResource(dummyObj, uri);

        Assert.assertEquals(dummyObj, result.getEntity());
    }


    @Test
    public void testEquals() {
        JsonApiResource result1 = new JsonApiResource(dummyObj,uri);
        JsonApiResource result2 = new JsonApiResource(dummyObj,uri);
        JsonApiResource resultOther = new JsonApiResource(new Dummy("x"), uri);

        Assert.assertEquals(result1, result2);
        Assert.assertNotEquals(resultOther, result1);
    }


    private class Dummy implements JsonApiIdentifiable {

        private String id;

        public Dummy (String id ) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }
    }
}
