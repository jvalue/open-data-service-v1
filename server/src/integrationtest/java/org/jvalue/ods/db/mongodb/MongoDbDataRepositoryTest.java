package org.jvalue.ods.db.mongodb;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentNotFoundException;
import org.jvalue.commons.db.data.Data;
import org.jvalue.commons.mongodb.test.AbstractRepositoryTest;
import org.jvalue.ods.db.mongodb.repositories.MongoDbDataRepository;

import java.util.*;

@SuppressWarnings("Duplicates")
public class MongoDbDataRepositoryTest extends AbstractRepositoryTest {

	private static final String DOMAIN_ID = "domainId";

	private MongoDbDataRepository repository;


	@Override
	protected void doCreateDatabase(DbConnectorFactory connectorFactory) {
		this.repository = new MongoDbDataRepository(connectorFactory, getClass().getSimpleName(), JsonPointer.compile("/" + DOMAIN_ID));
	}


	@Override
	protected void doDeleteDatabase(DbConnectorFactory dbConnectorFactory) {
		dbConnectorFactory.doDeleteDatabase(getClass().getSimpleName());
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


	@Test(expected = GenericDocumentNotFoundException.class)
	public void testNotFound() {
		repository.findByDomainId("someId");
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

		Map<String, JsonNode> result = repository.getBulk(ids);
		Assert.assertEquals(2, result.size());

		assertEquals(node1, result.get("id1"));
		assertEquals(node2, result.get("id2"));
	}


	@Test
	public void testBulkCreateAndUpdate() {
		JsonNode node1 = createObjectNode("id1", "hello");
		JsonNode node2 = createObjectNode("id2", "world");

		List<JsonNode> nodes = Arrays.asList(node1, node2);
		repository.writeBulk(nodes);

		List<JsonNode> createdNodes = repository.getAll();
		Assert.assertEquals(2, createdNodes.size());
		for (JsonNode node : createdNodes) {
			ObjectNode o = (ObjectNode) node;
			o.put("somethingElse", "foobar");
		}

		repository.writeBulk(createdNodes);
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
			data = repository.getPaginatedData(currentId, 3);
			fetchedNodes.addAll(data.getResult());
			currentId = data.getCursor().getNext();
		} while (data.getCursor().getHasNext());

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
