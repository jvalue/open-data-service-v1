package org.jvalue.ods.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

public class JsonUtilsTest {

    private final Dummy dummyObj = new Dummy();

    @Test
    public void testCreateRestrictedEntity() {
        JsonNode result = JsonUtils.createRestrictedEntity(dummyObj, "nestedDummy");

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.has("nestedDummy"));
        Assert.assertEquals(2, result.get("nestedDummy").size());
    }


    private class Dummy implements JsonApiIdentifiable {
        private final String id = "ID";
        private final int anotherAttribute = 1;
        private final NestedDummy nestedDummy = new NestedDummy();

        public int getAnotherAttribute() {
            return anotherAttribute;
        }

        public NestedDummy getNestedDummy() {
            return nestedDummy;
        }

        @Override
        public String getId() {
            return id;
        }
    }


    private class NestedDummy {
        public final String stringAttr = "String";
        public final int intAttr = 0;
    }
}
