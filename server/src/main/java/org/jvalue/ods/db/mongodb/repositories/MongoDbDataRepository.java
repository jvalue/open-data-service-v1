package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentOperationResult;
import org.jvalue.commons.db.data.Cursor;
import org.jvalue.commons.db.data.Data;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.value.commons.mongodb.AbstractMongoDbRepository;

import java.io.IOException;
import java.util.*;

public class MongoDbDataRepository extends AbstractMongoDbRepository implements GenericDataRepository<CouchDbDataView, JsonNode> {

	private static final String COLLECTION_NAME = "data";


	@Inject
	public MongoDbDataRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, CouchDbDataView.class);
	}


	@Override
	public JsonNode findByDomainId(String id) {
		Document document = database.getCollection(collectionName).find(Filters.eq("id", id)).first();
		JsonNode jsonNode = null;
		try {
			jsonNode = mapper.readValue(document.toJson(), JsonNode.class);
		} catch (IOException e) {

		}
		return jsonNode;
	}


	@Override
	public List<JsonNode> executeQuery(CouchDbDataView mongoDbQuery, String param) {
		return null;
	}


	@Override
	public void addQuery(CouchDbDataView mongoDbQuery) {

	}


	@Override
	public void removeQuery(CouchDbDataView mongoDbQuery) {

	}


	@Override
	public boolean containsQuery(CouchDbDataView mongoDbQuery) {
		return false;
	}


	@Override
	public Map<String, JsonNode> getBulk(Collection<String> ids) {
		return executeBulkGet(ids);
	}


	private Map<String, JsonNode> executeBulkGet(Collection<String> ids) {
		List<Bson> bsonFilterList = new ArrayList<>();
		for (String id : ids) {
			bsonFilterList.add(Filters.eq("id", id));
		}
		Bson selectAllIds = Filters.or(bsonFilterList);
		FindIterable<Document> documents = database.getCollection(collectionName).find(selectAllIds);


		Map<String, JsonNode> nodes = new HashMap<>();
		for (Document doc : documents) {
			JsonNode jsonNode = null;
			try {
				jsonNode = mapper.readValue(doc.toJson(), JsonNode.class);
			} catch (IOException e) {

			}
			nodes.put((String) doc.get("id"), jsonNode);
		}
		return nodes;
	}


	@Override
	public Collection<GenericDocumentOperationResult> writeBulk(Collection<JsonNode> data) {
		Collection<GenericDocumentOperationResult> result = new ArrayList<>();

		for (JsonNode jsonNode : data) {
			Document document = Document.parse(jsonNode.toString());
			try {
				database.getCollection(collectionName).insertOne(document);
			} catch (Exception e) {
				result.add(GenericDocumentOperationResult.newInstance((String) document.get("id"), e.getMessage(), e.getCause().getMessage()));
			}
		}
		return result;
	}


	@Override
	public Data getPaginatedData(String startDomainId, int count) {
		FindIterable<Document> documents = database.getCollection(collectionName).find().limit(count);
		List<JsonNode> jsonNodes = new ArrayList<>();
		int resultCount = 0;
		for (Document doc : documents) {
			JsonNode jsonNode = null;
			try {
				jsonNode = mapper.readValue(doc.toJson(), JsonNode.class);
			} catch (IOException e) {

			}
			jsonNodes.add(jsonNode);
			++resultCount;
		}
		Cursor cursor = new Cursor(null, false, resultCount);
		return new Data(jsonNodes, cursor);
	}


	@Override
	public void removeAll() {

	}


	@Override
	public void compact() {

	}
}
