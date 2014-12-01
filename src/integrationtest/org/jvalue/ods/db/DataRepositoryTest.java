package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.data.DataView;

public final class DataRepositoryTest extends AbstractDbTest {

	private static final String DOMAIN_ID = "domainId";

	private DataRepository repository;

	public DataRepositoryTest() {
		super(DataRepositoryTest.class.getSimpleName());
	}


	@Override
	protected void createDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		this.repository = new DataRepository(couchDbInstance, databaseName, JsonPointer.compile("/" + DOMAIN_ID));
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
		DataView view = createDbView();
		Assert.assertFalse(repository.containsView(view));
		repository.addView(view);
		Assert.assertTrue(repository.containsView(view));
	}


	@Test
	public void testExecuteView() {
		DataView view = createDbView();
		repository.addView(view);
		repository.add(createObjectNode("id1", "hello"));
		repository.add(createObjectNode("id2", "world"));

		Assert.assertEquals(2, repository.executeQuery(view, null).size());
		Assert.assertEquals(1, repository.executeQuery(view, "hello").size());
		Assert.assertEquals(1, repository.executeQuery(view, "world").size());
		Assert.assertEquals(0, repository.executeQuery(view, "foo").size());
	}


	private ObjectNode createObjectNode(String id, String otherProperty) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		node.put(DOMAIN_ID, id);
		node.put("somethingElse", otherProperty);
		return node;
	}


	private DataView createDbView() {
		return new DataView("testView", "function(doc) { if(doc.somethingElse) emit(doc.somethingElse, doc) }");
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
