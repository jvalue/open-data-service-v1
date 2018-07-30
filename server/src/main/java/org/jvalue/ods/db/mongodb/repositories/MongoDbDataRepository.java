package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentOperationResult;
import org.jvalue.commons.db.data.Data;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.mongodb.wrapper.JsonNodeEntity;
import org.value.commons.mongodb.AbstractMongoDbRepository;

import java.util.*;

public class MongoDbDataRepository extends AbstractMongoDbRepository implements GenericDataRepository<CouchDbDataView, JsonNode> {

	private static final String COLLECTION_NAME = "data";

	@Inject
	public MongoDbDataRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, CouchDbDataView.class);
	}


	@Override
	public JsonNode findByDomainId(String s) {
		return null;
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
//		List<Bson> bsonFilterList = new ArrayList<>();
//		for (String id: ids) {
//			bsonFilterList.add(("",""));
//		}
//		Filters.or()
//		database.getCollection(collectionName).find();
//		ViewQuery query = new ViewQuery()
//			.designDocId(DESIGN_DOCUMENT_ID)
//			.viewName(domainIdView.getId())
//			.includeDocs(true)
//			.keys(ids);
//
//		Map<String, JsonNode> nodes = new HashMap<>();
//		for (ViewResult.Row row : connector.queryView(query).getRows()) {
//			nodes.put(row.getKey(), row.getDocAsNode());
//		}
//		return nodes;
		return null;
	}


	@Override
	public Collection<GenericDocumentOperationResult> writeBulk(Collection<JsonNode> data) {
		return null;
	}


	@Override
	public Data getPaginatedData(String startDomainId, int count) {
		return null;
	}


	@Override
	public void removeAll() {

	}


	@Override
	public void compact() {

	}
}
