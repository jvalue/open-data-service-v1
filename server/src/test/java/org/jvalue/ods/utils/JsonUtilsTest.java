package org.jvalue.ods.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;
import org.jvalue.ods.rest.v2.jsonapi.document.JsonApiResourceIdentifierTest;

import java.io.IOException;
import java.util.Map;

import static org.jvalue.ods.utils.JsonUtils.getMapFromJson;

public class JsonUtilsTest {

    private final Dummy dummyObj = new Dummy();

    @Test
    public void testCreateRestrictedEntity() {
        JsonNode result = JsonUtils.createRestrictedEntity(dummyObj, "nestedDummy");

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.has("nestedDummy"));
        Assert.assertEquals(2, result.get("nestedDummy").size());
    }


    @Test
	public void testGetMapFromJson() throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode node = mapper.valueToTree(dummyObj);

    	Map<String, Object> result = getMapFromJson(node);

    	Assert.assertEquals("ID", result.get("id"));
    	Assert.assertEquals(1, result.get("anotherAttribute"));
    	Assert.assertTrue(result.containsKey("nestedDummy"));
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

		@Override
		public String getType() {
			return Dummy.class.getSimpleName();
		}
    }


    private class NestedDummy {
        public final String stringAttr = "String";
        public final int intAttr = 0;
    }
}
