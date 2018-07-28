package org.jvalue.ods.rest.v2.jsonapi.document;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

public class JsonApiResourceIdentifierTest {

    private final Dummy dummyObj = new Dummy("id_42");

    @Test
    public void testConstructor() {
        JsonApiResourceIdentifier result = new JsonApiResourceIdentifier(dummyObj);
        JsonApiResourceIdentifier result2 = new JsonApiResourceIdentifier(result);

        Assert.assertEquals("id_42", result.getId());
        Assert.assertEquals("Dummy", result.getType());

        Assert.assertEquals("id_42", result2.getId());
        Assert.assertEquals("Dummy", result2.getType());
    }


    @Test
    public void testEquals() {
        JsonApiResourceIdentifier result1 = new JsonApiResourceIdentifier(dummyObj);
        JsonApiResourceIdentifier result2 = new JsonApiResourceIdentifier(dummyObj);

        Assert.assertEquals(result1, result2);
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

        @Override
		public String getType() {
        	return Dummy.class.getSimpleName();
		}
    }
}
