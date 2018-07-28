package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentOperationResult;
import org.jvalue.commons.db.data.Data;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.utils.BsonToJsonUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MongoDbDataRepository implements GenericDataRepository<MongoDbQuery, JsonNode> {

	private final MongoDatabase database;
	private final MongoCollection<Document> mongObQueries;
	private static final String COLLECTION_NAME = "MongoDbQueries";


	@Inject
	public MongoDbDataRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName, @Assisted JsonPointer domainIdKey) {
		this.database = (MongoDatabase) dbConnectorFactory.createConnector(databaseName, true);
		this.mongObQueries = database.getCollection(COLLECTION_NAME);
	}


	@Override
	public JsonNode findByDomainId(String domainId) {
		Document findByIdQuery = new Document();
		findByIdQuery.put("_id", new ObjectId(domainId));
		//should only return one
		return BsonToJsonUtils.BsonToJson(mongObQueries.find(findByIdQuery));
	}


	@Override
	public List<JsonNode> executeQuery(MongoDbQuery mongoDbQuery, String param) {
		return null;
	}


	@Override
	public void addQuery(MongoDbQuery mongoDbQuery) {
		mongObQueries.insertOne(mongoDbQuery);
	}


	@Override
	public void removeQuery(MongoDbQuery mongoDbQuery) {
		mongObQueries.deleteOne(mongoDbQuery);
	}


	@Override
	public boolean containsQuery(MongoDbQuery mongoDbQuery) {
		return mongObQueries.find(mongoDbQuery) != null;
	}


	@Override
	public Map<String, JsonNode> getBulk(Collection<String> ids) {
//		Document query = new Document();
//
//		for(String id : ids){
//			query.append("_id", id);
//		}
//
//		Map<String, JsonNode> nodes = new HashMap<>();
//
//		for (Document doc : mongObQueries.find(query)) {
//			nodes.put(, doc.toJson());
//		}
//		return nodes;
//		List<Document> list = new ArrayList<>();
//		for (String id : ids){
//			list.add(new Document());
//		}
//		mongObQueries.bulkWrite()
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
