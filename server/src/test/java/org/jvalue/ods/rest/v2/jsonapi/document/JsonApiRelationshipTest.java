package org.jvalue.ods.rest.v2.jsonapi.document;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

public class JsonApiRelationshipTest {

	private final URI testLocation = URI.create("http://www.test.com");
	private final Dummy dummy1 = new Dummy("1");
	private final Dummy dummy2 = new Dummy("2");
	private final AnotherDummyClass anotherDummyClass = new AnotherDummyClass("2");
	private final List<JsonApiIdentifiable> relatedList = new LinkedList<>();


	@Before
	public void setUp() {
		relatedList.add(dummy1);
		relatedList.add(anotherDummyClass);
	}


	@Test
	public void testContainsEntity() {
		JsonApiRelationship result = new JsonApiRelationship(relatedList, testLocation);

		Assert.assertTrue(result.containsEntity(dummy1));
		Assert.assertTrue(result.containsEntity(anotherDummyClass));
		Assert.assertFalse(result.containsEntity(dummy2));
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
