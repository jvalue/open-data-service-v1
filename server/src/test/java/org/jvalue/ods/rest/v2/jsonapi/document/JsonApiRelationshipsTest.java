package org.jvalue.ods.rest.v2.jsonapi.document;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;

public class JsonApiRelationshipsTest {

	private final URI testLocation = URI.create("http://www.test.com");

	private final URI anotherTestLocation = URI.create("http://www.test.de");

	private final Dummy dummy1 = new Dummy("1");

	private final Dummy dummy2 = new Dummy("2");

	private final AnotherDummyClass anotherDummyClass = new AnotherDummyClass("2");


	@Test
	public void addRelationship() {

		JsonApiResourceIdentifier dummy1Identifier = new JsonApiResourceIdentifier(dummy1);
		JsonApiResourceIdentifier dummy2Identifier = new JsonApiResourceIdentifier(dummy2);

		JsonApiRelationships result = new JsonApiRelationships();
		result.addRelationship("First", dummy1, testLocation);
		result.addRelationship("Second", dummy2, anotherTestLocation);


		JsonApiRelationships.Relationship resultFirst = result.getRelationships().get("First");
		JsonApiRelationships.Relationship resultSecond = result.getRelationships().get("Second");

		Assert.assertEquals(2, result.getRelationships().size());
		Assert.assertEquals(dummy1Identifier, resultFirst.getData().get(0));
		Assert.assertEquals(dummy2Identifier, resultSecond.getData().get(0));
		Assert.assertEquals(testLocation, resultFirst.getLinks().get(JsonLinks.RELATED));
		Assert.assertEquals(anotherTestLocation, resultSecond.getLinks().get(JsonLinks.RELATED));
	}


	@Test
	public void testHasRelationshipTo() {
		JsonApiRelationships result = new JsonApiRelationships();
		result.addRelationship("dummy1", dummy1, testLocation);
		result.addRelationship("anotherDummyClass", anotherDummyClass, testLocation);

		Assert.assertTrue(result.hasRelationshipTo(dummy1));
		Assert.assertTrue(result.hasRelationshipTo(anotherDummyClass));
		Assert.assertFalse(result.hasRelationshipTo(dummy2));
	}


	@Test
	public void testGetRelationshipURI() {
		JsonApiRelationships result = new JsonApiRelationships();
		result.addRelationship("dummy1", dummy1, testLocation);

		Assert.assertEquals(testLocation, result.getRelationshipUri(dummy1));
	}


	@Test
	public void testGetRelationshipURIWithUnrelatedDummy() {
		JsonApiRelationships result = new JsonApiRelationships();
		result.addRelationship("dummy2", dummy2, testLocation);

		Assert.assertEquals(null,result.getRelationshipUri(dummy1));
	}


	private class Dummy implements JsonApiIdentifiable {

		private final String id;


		public Dummy(String id) {
			this.id = id;
		}


		@Override
		public String getId() {
			return id;
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
	}
}
