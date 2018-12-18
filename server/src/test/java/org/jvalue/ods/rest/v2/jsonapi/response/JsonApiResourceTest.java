package org.jvalue.ods.rest.v2.jsonapi.response;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.net.URI;

public class JsonApiResourceTest {

    private final Dummy dummyObj = new Dummy("id_01");
	private final Dummy dummy02 = new Dummy("id_02");
	private final Dummy dummy03 = new Dummy("id_03");
    private final AnotherDummyClass anotherDummyClass = new AnotherDummyClass("id_02");
    private final URI uri = URI.create("http://test.com");
    private final URI anotherUri = URI.create("http://test.org");


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


	@Test
	public void addRelationship() {

		JsonApiResourceIdentifier dummy1Identifier = new JsonApiResourceIdentifier(dummy02);
		JsonApiResourceIdentifier dummy2Identifier = new JsonApiResourceIdentifier(dummy03);

		JsonApiResource result = new JsonApiResource(dummyObj,uri);
		result.addRelationship("First", dummy02, uri);
		result.addRelationship("Second", dummy03, anotherUri);


		JsonApiRelationship resultFirst = result.getRelationships().get("First");
		JsonApiRelationship resultSecond = result.getRelationships().get("Second");

		Assert.assertEquals(2, result.getRelationships().size());
		Assert.assertEquals(dummy1Identifier, resultFirst.getData().get(0));
		Assert.assertEquals(dummy2Identifier, resultSecond.getData().get(0));
		Assert.assertEquals(uri, resultFirst.getLinks().get(JsonLinks.RELATED));
		Assert.assertEquals(anotherUri, resultSecond.getLinks().get(JsonLinks.RELATED));
	}


	@Test
	public void testHasRelationshipTo() {
		JsonApiResource result = new JsonApiResource(dummyObj, uri);
		result.addRelationship("dummy02", dummy02, uri);
		result.addRelationship("anotherDummyClass", anotherDummyClass, uri);

		Assert.assertTrue(result.hasRelationshipTo(dummy02));
		Assert.assertTrue(result.hasRelationshipTo(anotherDummyClass));
		Assert.assertFalse(result.hasRelationshipTo(dummy03));
	}


	@Test
	public void testGetRelationshipURI() {
		JsonApiResource result = new JsonApiResource(dummyObj, uri);
		result.addRelationship("dummy1", dummy02, uri);

		Assert.assertEquals(uri, result.getRelationshipUri(dummy02));
	}


	@Test
	public void testGetRelationshipURIWithUnrelatedDummy() {
		JsonApiResource result = new JsonApiResource(dummyObj, uri);
		result.addRelationship("dummy2", dummy02, uri);

		Assert.assertNull(result.getRelationshipUri(dummy03));
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


	private class AnotherDummyClass implements JsonApiIdentifiable {

		private final String id;

		public AnotherDummyClass(String id) {
			this.id = id;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getType() {
			return AnotherDummyClass.class.getSimpleName();
		}
	}
}
