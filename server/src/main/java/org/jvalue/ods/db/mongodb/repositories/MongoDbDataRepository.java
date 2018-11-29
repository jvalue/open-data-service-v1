package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.bson.types.ObjectId;
import org.jongo.MongoCursor;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentOperationResult;
import org.jvalue.commons.db.data.Cursor;
import org.jvalue.commons.db.data.Data;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.value.commons.mongodb.AbstractMongoDbRepository;

import java.util.*;

public class MongoDbDataRepository extends AbstractMongoDbRepository<JsonNode> implements GenericDataRepository<JsonNode> {

	private final JsonPointer domainIdKey;


	@Inject
	public MongoDbDataRepository(DbConnectorFactory connectorFactory, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		super(connectorFactory, databaseName, "DataRepositoryCollection", JsonNode.class);
		this.domainIdKey = domainIdKey;
	}

	@Override
	protected String createIdFilter(String Id) {
		return "{ "+domainIdKey.getMatchingProperty()+" : '" + Id + "' }";
	}

	@Override
	public JsonNode findByDomainId(String domainId) {
		JsonNode byId = findById(domainId);
		return removeObjectId(byId);
	}


	@Override
	public Map<String, JsonNode> getBulk(Collection<String> ids) {
		return executeBulkGet(ids);
	}

	public Map<String, JsonNode> executeBulkGet(Collection<String> ids) {
		MongoCursor<JsonNode> jsonNodeCurser = jongo.getCollection(collectionName).find("{"+domainIdKey.getMatchingProperty()+" : {$in:#}}", ids).as(JsonNode.class);

		Map<String, JsonNode> nodes = new HashMap<>();
		for (JsonNode node : jsonNodeCurser) {
			nodes.put(node.at(domainIdKey).asText(), node);
		}
		return nodes;
	}


	@Override
	public Collection<GenericDocumentOperationResult> writeBulk(Collection<JsonNode> data) {
		Collection<GenericDocumentOperationResult> result = new ArrayList<>();

		for (JsonNode jsonNode : data) {
			try {
				add(jsonNode);
			} catch (Exception e) {
				result.add(GenericDocumentOperationResult.newInstance(jsonNode.get("id").toString(), e.getMessage(), e.getCause().getMessage()));
			}
		}
		return result;
	}


	@Override
	public Data getPaginatedData(String startDomainId, int count) {
		MongoCursor<JsonNode> documents;
		if (startDomainId != null) {
			ObjectId objectId = new ObjectId(startDomainId);
			documents = jongo.getCollection(collectionName).find("{_id : {$gte : # }}", objectId).limit(count + 1).as(JsonNode.class);
		} else {
			documents = jongo.getCollection(collectionName).find().limit(count + 1).as(JsonNode.class);
		}
		List<JsonNode> jsonNodes = new ArrayList<>();
		int resultCount = 0;
		String nextStartDomainId = null;
		boolean hasNext = false;

		for (JsonNode jsonNode : documents) {
			if (resultCount == count) {
				hasNext = true;
				nextStartDomainId = jsonNode.get("_id").toString();
				break;
			}
			ObjectNode objectNode = removeObjectId(jsonNode);

			jsonNodes.add(objectNode);
			++resultCount;
		}
		Cursor cursor = new Cursor(nextStartDomainId, hasNext, resultCount);
		return new Data(jsonNodes, cursor);
	}


	@Override
	public Data getAllDocuments() {
		List<JsonNode> all = getAll();
		List<JsonNode> resultNodes = new ArrayList<>();

		all.forEach((jsonNode) -> {
			ObjectNode objectNode = removeObjectId(jsonNode);
			resultNodes.add(objectNode);
			}
		);

		Cursor cursor = new Cursor(null, false, all.size() -1 );
		return new Data(resultNodes, cursor);
	}


	private ObjectNode removeObjectId(JsonNode jsonNode) {
		//remove unwanted ObjectIdField
		ObjectNode objectNode = jsonNode.deepCopy();
		objectNode.remove("_id");
		return objectNode;
	}


	@Override
	public void removeAll() {
		List<JsonNode> all = getAll();
		for (JsonNode jsonNode : all) {
			remove(jsonNode);
		}
	}


	@Override
	public void compact() {

	}


	@Override
	protected String getValueId(JsonNode Value) {
		return Value.at(domainIdKey).asText();
	}
}
