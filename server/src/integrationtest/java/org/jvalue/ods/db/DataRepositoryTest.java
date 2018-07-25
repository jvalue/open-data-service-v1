package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.test.AbstractRepositoryTest;
import org.jvalue.ods.api.data.Data;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.DataRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class DataRepositoryTest extends AbstractRepositoryTest {

	private static final String DOMAIN_ID = "domainId";

	private DataRepository repository;


	@Override
	protected void doCreateDatabase(DbConnectorFactory connectorFactory) {
		this.repository = new DataRepository(connectorFactory, getClass().getSimpleName(), JsonPointer.compile("/" + DOMAIN_ID));
	}


	@Test
	public void testFindValidId() {
		JsonNode node1 = createObjectNode("id1", "hello");
		JsonNode node2 = createObjectNode("id2", "world");
		repository.add(node1);
		repository.add(node2);

		JsonNode node = repository.findByDomainId("id1");
		assertEquals(node, node1);

		node = repository.findByDomainId("id2");
		assertEquals(node, node2);
	}


	@Test(expected = DocumentNotFoundException.class)
	public void testNotFound() {
		repository.findByDomainId("someId");
	}


	@Test(expected = IllegalStateException.class)
	public void testDuplicateId() {
		repository.add(createObjectNode("id", "hello"));
		repository.add(createObjectNode("id", "world"));
		repository.findByDomainId("id");
	}


	@Test
	public void testCreateView() {
		CouchDbDataView view = createDbView();
		Assert.assertFalse(repository.containsQuery(view));
		repository.addQuery(view);
		Assert.assertTrue(repository.containsQuery(view));
	}


	@Test
	public void testExecuteView() {
		CouchDbDataView view = createDbView();
		repository.addQuery(view);
		repository.add(createObjectNode("id1", "hello"));
		repository.add(createObjectNode("id2", "world"));

		Assert.assertEquals(2, repository.executeQuery(view, null).size());
		Assert.assertEquals(1, repository.executeQuery(view, "hello").size());
		Assert.assertEquals(1, repository.executeQuery(view, "world").size());
		Assert.assertEquals(0, repository.executeQuery(view, "foo").size());
	}


	@Test
	public void testBulkGet() {
		JsonNode node1 = createObjectNode("id1", "hello");
		JsonNode node2 = createObjectNode("id2", "world");
		repository.add(node1);
		repository.add(node2);

		JsonNode node = repository.findByDomainId("id1");
		assertEquals(node, node1);

		Collection<String> ids = new LinkedList<>();
		ids.add("id1");
		ids.add("id2");

		Map<String, JsonNode> result = repository.executeBulkGet(ids);
		Assert.assertEquals(2, result.size());

		assertEquals(node1, result.get("id1"));
		assertEquals(node2, result.get("id2"));
	}


	@Test
	public void testBulkCreateAndUpdate() {
		JsonNode node1 = createObjectNode("id1", "hello");
		JsonNode node2 = createObjectNode("id2", "world");

		List<JsonNode> nodes = Arrays.asList(node1, node2);
		repository.executeBulkCreateAndUpdate(nodes);

		List<JsonNode> createdNodes = repository.getAll();
		Assert.assertEquals(2, createdNodes.size());
		for (JsonNode node : createdNodes) {
			ObjectNode o = (ObjectNode) node;
			o.put("somethingElse", "foobar");
		}

		repository.executeBulkCreateAndUpdate(createdNodes);
		for (JsonNode node : createdNodes) {
			Assert.assertEquals("foobar", node.get("somethingElse").asText());
		}
	}


	@Test
	public void testExecutePaginatedGet() {
		List<JsonNode> nodes = new LinkedList<>();
		int nodeCount = 10;
		for (int i = 0; i < nodeCount; ++i) {
			JsonNode node = createObjectNode("id" + i, "data" + i);
			repository.add(node);
			nodes.add(node);
		}

		List<JsonNode> fetchedNodes = new LinkedList<>();
		Data data;
		String currentId = null;
		do {
			data = repository.executePaginatedGet(currentId, 3);
			fetchedNodes.addAll(data.getResult());
			currentId = data.getCursor().getNext();
		} while(data.getCursor().getHasNext());

		Assert.assertEquals(nodes, fetchedNodes);
	}


	@Test
	public void testRemoveAll() {
		repository.add(createObjectNode("id1", "hello"));
		repository.add(createObjectNode("id2", "world"));

		Assert.assertEquals(2, repository.getAll().size());
		repository.removeAll();
		Assert.assertEquals(0, repository.getAll().size());
	}


	private ObjectNode createObjectNode(String id, String otherProperty) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		node.put(DOMAIN_ID, id);
		node.put("somethingElse", otherProperty);
		return node;
	}


	private CouchDbDataView createDbView() {
		return new CouchDbDataView("testView", "function(doc) { if(doc.somethingElse) emit(doc.somethingElse, doc) }");
	}


	private void assertEquals(JsonNode node1, JsonNode node2) {
		Assert.assertEquals(removeIdAndRev(node1), removeIdAndRev(node2));
	}


	private JsonNode removeIdAndRev(JsonNode node) {
		ObjectNode object = (ObjectNode) node;
		object.remove("_id");
		object.remove("_rev");
		return object;
	}

}
