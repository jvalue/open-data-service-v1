package org.jvalue.ods.rest.v2.jsonapi.response;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.jvalue.ods.rest.v2.TestUtils.COMBINED_MESSAGE;
import static org.jvalue.ods.rest.v2.TestUtils.ERRCODE_VALID;
import static org.jvalue.ods.rest.v2.TestUtils.MESSAGE;

public class JsonApiDocumentTest {

	private final Dummy dummyObj01 = new Dummy("id_01");
	private final Dummy dummyObj42 = new Dummy("id_42");
	private final Dummy dummyObj43 = new Dummy("id_43");
	private final URI uri = URI.create("http://test.com");
	private final String linkName = "testLink";

	@Mocked
	private UriInfo uriInfoMock;

	private void setUpUriInfo() {
		new Expectations() {{
			uriInfoMock.getAbsolutePath();
			result = uri;
		}};
	}


	@Test
    public void testConstructorWithEntity() {
		setUpUriInfo();
    	JsonApiDocument result = new JsonApiDocument(dummyObj01, uriInfoMock);

    	JsonApiResource resource = result.getData().get(0);

    	Assert.assertEquals(1, result.getData().size());
    	Assert.assertEquals(dummyObj01, resource.getEntity());
    }


    @Test
    public void testConstructorWithCollection() {
		setUpUriInfo();
		Collection<JsonApiIdentifiable> dummyCollection = Arrays.asList(dummyObj01, dummyObj42, dummyObj43);

		JsonApiDocument result = new JsonApiDocument(dummyCollection, uriInfoMock);

		Assert.assertEquals(dummyCollection,
				result
						.getData()
						.stream()
						.map(JsonApiResource::getEntity)
						.collect(Collectors.toList()));
	}


	@Test
	public void testConstructorWithError() {
		JsonApiError error = new JsonApiError(MESSAGE, ERRCODE_VALID);

		JsonApiDocument result = new JsonApiDocument(error);

		Assert.assertEquals(0, result.data.size());
		Assert.assertEquals(1, result.errors.size());
		Assert.assertEquals(ERRCODE_VALID, result.errors.get(0).getCode());
		Assert.assertEquals(MESSAGE, result.errors.get(0).getMessage());
	}


	@Test
	public void testSetResourceCollection() {
		setUpUriInfo();
		URI collectionURI = URI.create("http://localhost:8080/path/to/collection/");
		Collection<JsonApiIdentifiable> dummyCollection = Arrays.asList(dummyObj01, dummyObj42, dummyObj43);

		JsonApiDocument result = new JsonApiDocument(dummyCollection, uriInfoMock);
		result.setResourceCollectionURI(collectionURI);

		result.data.stream().allMatch(r -> r.getLinks().size() == 1);
		Assert.assertEquals(collectionURI.resolve("id_01"), result.getData().get(0).getSelfLink());
		Assert.assertEquals(collectionURI.resolve("id_42"), result.getData().get(1).getSelfLink());
		Assert.assertEquals(collectionURI.resolve("id_43"), result.getData().get(2).getSelfLink());

	}


	@Test
    public void testLinks() {
		setUpUriInfo();
		new Expectations() {{
			uriInfoMock.getRequestUri();
			result = uri;
		}};

		JsonApiDocument result = new JsonApiDocument(dummyObj01, uriInfoMock);
		result.addSelfLink();

		result.addLink(linkName, uri);

		Assert.assertEquals(2, result.getLinks().size());
		Assert.assertEquals(uri , result.getLinks().get(linkName));
		assertSelfLinkExists(result);
	}


	@Test
	public void hasRelationshipTo() {
		setUpUriInfo();
		JsonApiDocument document = new JsonApiDocument(dummyObj01, uriInfoMock);
		document.addRelationship("rel1", dummyObj42, uri);

		Assert.assertTrue(document.hasRelationshipTo(dummyObj42));
		Assert.assertFalse(document.hasRelationshipTo(dummyObj43));
	}


    @Test
    public void testEquals() {
		setUpUriInfo();
		JsonApiDocument result1 = new JsonApiDocument(dummyObj01, uriInfoMock);
		JsonApiDocument result2 = new JsonApiDocument(dummyObj01, uriInfoMock);
		JsonApiDocument resultOther = new JsonApiDocument(dummyObj42, uriInfoMock);

		Assert.assertEquals(result1, result2);
		Assert.assertNotEquals(resultOther, result1);
	}


    private void assertSelfLinkExists(JsonApiDocument doc) {
		Assert.assertEquals(uri, doc.getSelfLink());
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

		@Override
		public String getType() {
			return Dummy.class.getSimpleName();
		}
	}
}
