/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.response;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    	JsonApiData data = result.getData();

    	Assert.assertTrue(data instanceof JsonApiResource);
    	Assert.assertEquals(dummyObj01, data.asSingleResource().getEntity());
    }


    @Test
    public void testConstructorWithCollection() {
		setUpUriInfo();
		List<JsonApiIdentifiable> dummyCollection = Arrays.asList(dummyObj01, dummyObj42, dummyObj43);

		JsonApiDocument result = new JsonApiDocument(dummyCollection, uriInfoMock);

		Assert.assertEquals(
			dummyCollection,
			result
				.getData()
				.asResourceCollection()
				.getEntity()
				.stream()
				.map(JsonApiResource::getEntity)
				.collect(Collectors.toList()));
	}


	@Test
	public void testConstructorWithError() {
		JsonApiError error = new JsonApiError(MESSAGE, ERRCODE_VALID);

		JsonApiDocument result = new JsonApiDocument(error);

		Assert.assertNull(result.data);
		Assert.assertEquals(1, result.errors.size());
		Assert.assertEquals(ERRCODE_VALID, result.errors.get(0).getCode());
		Assert.assertEquals(MESSAGE, result.errors.get(0).getMessage());
	}


	@Test
	public void testSetResourceCollection() {
		setUpUriInfo();
		URI collectionURI = URI.create("http://localhost:8080/path/to/collection/");
		List<JsonApiIdentifiable> dummyCollection = Arrays.asList(dummyObj01, dummyObj42, dummyObj43);

		JsonApiDocument result = new JsonApiDocument(dummyCollection, uriInfoMock);
		result.setResourceCollectionURI(collectionURI);

		List<JsonApiResource> entityList = result.getData().asResourceCollection().getEntity();

		Assert.assertTrue(
			entityList
				.stream()
				.allMatch(r -> r.getLinks().size() == 1)
		);
		Assert.assertEquals(collectionURI.resolve("id_01"), entityList.get(0).getSelfLink());
		Assert.assertEquals(collectionURI.resolve("id_42"), entityList.get(1).getSelfLink());
		Assert.assertEquals(collectionURI.resolve("id_43"), entityList.get(2).getSelfLink());

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
