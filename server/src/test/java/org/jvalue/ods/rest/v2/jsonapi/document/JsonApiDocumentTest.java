package org.jvalue.ods.rest.v2.jsonapi.document;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class JsonApiDocumentTest {

	private final Dummy dummyObj01 = new Dummy("id_01");
	private final Dummy dummyObj42 = new Dummy("id_42");
	private final Dummy dummyObj43 = new Dummy("id_43");
	private final URI uri = URI.create("http://test.com");
	private final String linkName = "testLink";

	@Mocked
	private UriInfo uriInfoMock;

	@Before
	public void setUp() {
		new Expectations() {{
			uriInfoMock.getAbsolutePath();
			result = uri;
		}};
	}


	@Test
    public void testConstructorWithEntity() {
    	JsonApiDocument result = new JsonApiDocument(dummyObj01, uriInfoMock);

    	JsonApiResource resource = (JsonApiResource) result.getData().get(0);
    	Assert.assertEquals(dummyObj01, resource.getEntity());
		assertSelfLinkExists(result);
    }


    @Test
    public void testConstructorWithCollection() {
		Collection<JsonApiIdentifiable> dummyCollection = new LinkedList<>();
		dummyCollection.add(dummyObj01);
		dummyCollection.add(dummyObj42);
		dummyCollection.add(dummyObj43);

		JsonApiDocument result = new JsonApiDocument(dummyCollection, uriInfoMock);

		Assert.assertEquals(dummyCollection,
				result
						.getData()
						.stream()
						.map(r -> ((JsonApiResource) r).getEntity())
						.collect(Collectors.toList()));
		assertSelfLinkExists(result);
	}


    @Test
    public void testToIdentifier() {
		JsonApiDocument resultToIdentifier = new JsonApiDocument(dummyObj01, uriInfoMock);
		JsonApiDocument unmodifiedResult = new JsonApiDocument(dummyObj01, uriInfoMock);

		resultToIdentifier.toIdentifier();


		JsonApiResourceIdentifier identifierData = resultToIdentifier.getData().get(0);
		JsonApiResourceIdentifier unmodifiedData = unmodifiedResult.getData().get(0);

		Assert.assertFalse(identifierData instanceof JsonApiResource);
		Assert.assertTrue(unmodifiedData instanceof JsonApiResource);
		Assert.assertEquals(dummyObj01.getId(), identifierData.getId());
		Assert.assertEquals(dummyObj01.getId(), unmodifiedData.getId());
		Assert.assertEquals("Dummy", identifierData.getType());
		Assert.assertEquals("Dummy", unmodifiedData.getType());
		assertSelfLinkExists(resultToIdentifier);
		assertSelfLinkExists(unmodifiedResult);
	}


    @Test
    public void testLinks() {
		JsonApiDocument result = new JsonApiDocument(dummyObj01, uriInfoMock);

		result.addLink(linkName, uri);

		Assert.assertEquals(2, result.getLinks().size());
		Assert.assertEquals(uri, result.getLinks().get("self"));
		assertSelfLinkExists(result);
	}


    @Test
    public void testEquals() {
		JsonApiDocument result1 = new JsonApiDocument(dummyObj01, uriInfoMock);
		JsonApiDocument result2 = new JsonApiDocument(dummyObj01, uriInfoMock);
		JsonApiDocument resultOther = new JsonApiDocument(dummyObj42, uriInfoMock);

		Assert.assertEquals(result1, result2);
		Assert.assertNotEquals(resultOther, result1);
	}


    private void assertSelfLinkExists(JsonApiDocument doc) {
		Assert.assertEquals(uri, doc.getLinks().get("self"));
	}


	private class Dummy implements JsonApiIdentifiable {

        private String id;

        private Dummy (String id ) {
            this.id = id;
        }


		@Override
		public String getId() {
			return id;
		}
	}
}
