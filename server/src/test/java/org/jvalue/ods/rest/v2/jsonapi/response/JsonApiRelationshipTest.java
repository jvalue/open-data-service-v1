package org.jvalue.ods.rest.v2.jsonapi.response;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks.RELATED;

public class JsonApiRelationshipTest {

	private final URI testLocation = URI.create("http://www.test.com");
	private final URI anotherLocation = URI.create("http://www.test.org");
	private final Dummy dummy1 = new Dummy("1");
	private final Dummy dummy2 = new Dummy("2");
	private final AnotherDummyClass anotherDummyClass = new AnotherDummyClass("2");
	private final List<JsonApiIdentifiable> relatedList = Arrays.asList(dummy1, anotherDummyClass);


	@Test
	public void testContainsEntity() {
		JsonApiRelationship result = new JsonApiRelationship(relatedList, testLocation);

		Assert.assertTrue(result.containsEntity(dummy1));
		Assert.assertTrue(result.containsEntity(anotherDummyClass));
		Assert.assertFalse(result.containsEntity(dummy2));
	}


	@Test
	public void testGetData() {
		JsonApiRelationship result = new JsonApiRelationship(relatedList, testLocation);


		Assert.assertEquals(2, result.getData().size());
		Assert.assertEquals(new JsonApiResourceIdentifier(dummy1), result.getData().get(0));
		Assert.assertEquals(new JsonApiResourceIdentifier(anotherDummyClass), result.getData().get(1));
	}


	@Test
	public void testGetLinks() {
		JsonApiRelationship result = new JsonApiRelationship(dummy1, testLocation);

		Assert.assertEquals(testLocation, result.getLinks().get(RELATED));
	}


	@Test
	public void testEquals() {
		List<JsonApiIdentifiable> anotherList = Arrays.asList(dummy1, dummy2);

		JsonApiRelationship result1 = new JsonApiRelationship(relatedList, testLocation);
		JsonApiRelationship result2 = new JsonApiRelationship(relatedList, testLocation);
		JsonApiRelationship result3 = new JsonApiRelationship(relatedList, anotherLocation);
		JsonApiRelationship result4 = new JsonApiRelationship(anotherList, testLocation);

		Assert.assertTrue(result1.equals(result2));
		Assert.assertFalse(result1.equals(result3));
		Assert.assertFalse(result1.equals(result4));
		Assert.assertFalse(result3.equals(result4));
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
