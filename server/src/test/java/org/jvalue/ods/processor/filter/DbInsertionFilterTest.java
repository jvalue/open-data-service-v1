package org.jvalue.ods.processor.filter;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;
import org.jvalue.ods.db.couchdb.DataRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class DbInsertionFilterTest {

	private static final String
			DOMAIN_ID = "domainId",
			DB_ID = "_id",
			DB_REV = "_rev",
			VALUE_DOMAIN_ID = "someDomainId",
			VALUE_ID = "someId",
			VALUE_REV = "someRev";

	private final DataSource source = new DataSource(
			"someId",
			JsonPointer.compile("/" + DOMAIN_ID),
			new ObjectNode(JsonNodeFactory.instance),
			new DataSourceMetaData("", "", "", "", "", "", ""));

	@Mocked private MetricRegistry registry;



	@Test
	@SuppressWarnings("unchecked")
	public void testAdd(
			@Mocked final DataRepository repository) throws Exception {

		AbstractFilter<ObjectNode, ObjectNode> filter = new DbInsertionFilter(repository, source, true, registry);
		filter.filter(createObject(VALUE_DOMAIN_ID));
		filter.onComplete();

		new Verifications() {{
			repository.executeBulkCreateAndUpdate((List<JsonNode>) any); times = 1;
		}};
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testUpdate(
			@Mocked final DataRepository repository) throws Exception {

		new Expectations() {{
			Collection<String> keys = new LinkedList<>();
			keys.add(VALUE_DOMAIN_ID);
			Map<String, JsonNode> nodes = new HashMap<>();
			nodes.put(VALUE_DOMAIN_ID, addDbIdAndRev(createObject(VALUE_DOMAIN_ID)));

			repository.executeBulkGet(keys);
			result = nodes;
		}};

		AbstractFilter<ObjectNode, ObjectNode> filter = new DbInsertionFilter(repository, source, true, registry);
		filter.filter(createObject(VALUE_DOMAIN_ID));
		filter.onComplete();

		new Verifications() {{
			Collection<JsonNode> nodeList;
			repository.executeBulkCreateAndUpdate(nodeList = withCapture()); times = 1;

			for (JsonNode node : nodeList) {
				Assert.assertTrue(node instanceof ObjectNode);
				ObjectNode object = (ObjectNode) node;
				Assert.assertEquals(VALUE_ID, object.get(DB_ID).asText());
				Assert.assertEquals(VALUE_REV, object.get(DB_REV).asText());
			}
		}};
	}


	private ObjectNode createObject(String domainId) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		node.put(DOMAIN_ID, domainId);
		return node;
	}


	private ObjectNode addDbIdAndRev(ObjectNode node) {
		node.put(DB_ID, VALUE_ID);
		node.put(DB_REV, VALUE_REV);
		return node;
	}
}
