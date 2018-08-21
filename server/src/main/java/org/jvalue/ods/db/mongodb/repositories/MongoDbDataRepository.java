package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentOperationResult;
import org.jvalue.commons.db.data.Cursor;
import org.jvalue.commons.db.data.Data;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.value.commons.mongodb.AbstractMongoDbRepository;
import org.value.commons.mongodb.MongoDbDocument;
import org.value.commons.mongodb.MongoDbDocumentAdaptable;
import org.value.commons.mongodb.MongoDbRepositoryAdapter;

import java.io.IOException;
import java.util.*;

public class MongoDbDataRepository extends MongoDbRepositoryAdapter<
	MongoDbDataRepository.MongoDbDataRepositoryImpl,
	MongoDbDataRepository.MongoDbDataDocument,
	JsonNode> implements GenericDataRepository<CouchDbDataView, JsonNode> {

	@Inject
	public MongoDbDataRepository(DbConnectorFactory connectorFactory, @Assisted String databaseName) {
		super(new MongoDbDataRepositoryImpl(connectorFactory, databaseName, "DataRepositoryCollection"));
	}


	@Override
	public JsonNode findByDomainId(String domainId) {
		return repository.findById(domainId).getValue();
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
		return repository.executeBulkGet(ids);
	}


	@Override
	public Collection<GenericDocumentOperationResult> writeBulk(Collection<JsonNode> data) {
		return repository.writeBulk(data);
	}


	@Override
	public Data getPaginatedData(String startDomainId, int count) {
		return repository.getPaginatedData(startDomainId, count);
	}


	@Override
	public void removeAll() {
		List<MongoDbDataDocument> all = repository.getAll();
		for (MongoDbDataDocument doc : all) {
			repository.remove(doc);
		}
	}


	@Override
	public void compact() {

	}


	static class MongoDbDataRepositoryImpl extends AbstractMongoDbRepository<MongoDbDataDocument> implements MongoDbDocumentAdaptable<MongoDbDataDocument, JsonNode> {

		protected MongoDbDataRepositoryImpl(DbConnectorFactory connectorFactory, String databaseName, String collectionName) {
			super(connectorFactory, databaseName, collectionName, MongoDbDataDocument.class);
		}


		@Override
		protected MongoDbDataDocument createNewDocument(Document document) {
			return new MongoDbDataDocument(document);
		}


		@Override
		protected String getValueId(MongoDbDataDocument Value) {
			return Value.getValue().get("id").asText();
		}


		@Override
		public MongoDbDataDocument createDbDocument(JsonNode value) {
			return new MongoDbDataDocument(value);
		}


		@Override
		public String getIdForValue(JsonNode value) {
			return value.get("value").get("id").toString();
		}


		@Override
		protected Bson createIdFilter(String Id){
			int id = Integer.parseInt(Id);
			return Filters.eq("value.id", id);
		}


		public Map<String, JsonNode> executeBulkGet(Collection<String> ids) {

			List<Bson> bsonFilterList = new ArrayList<>();
			for (String id : ids) {
				bsonFilterList.add(createIdFilter(id));
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
				nodes.put(((Document) doc.get("value")).get("id").toString(), jsonNode.get("value"));
			}
			return nodes;
		}


		public Collection<GenericDocumentOperationResult> writeBulk(Collection<JsonNode> data) {
			Collection<GenericDocumentOperationResult> result = new ArrayList<>();

			for (JsonNode jsonNode : data) {
				MongoDbDataDocument dbDocument = createDbDocument(jsonNode);
				try {
					add(dbDocument);
				} catch (Exception e) {
					result.add(GenericDocumentOperationResult.newInstance((String) dbDocument.get("id"), e.getMessage(), e.getCause().getMessage()));
				}
			}
			return result;
		}


		public Data getPaginatedData(String startDomainId, int count) {

			FindIterable<Document> documents;
			if (startDomainId != null) {
				ObjectId objectId = new ObjectId(startDomainId);
				documents = database.getCollection(collectionName).find(Filters.gte("_id", objectId)).limit(count + 1);
			} else {
				documents = database.getCollection(collectionName).find().limit(count + 1);
			}
			List<JsonNode> jsonNodes = new ArrayList<>();
			int resultCount = 0;
			String nextStartDomainId = null;
			boolean hasNext = false;

			for (Document doc : documents) {
				if (resultCount == count) {
					hasNext = true;
					nextStartDomainId = doc.get("_id").toString();
					break;
				}

				JsonNode jsonNode = null;
				try {
					jsonNode = mapper.readValue(doc.toJson(), JsonNode.class);
				} catch (IOException e) {

				}
				jsonNodes.add(jsonNode.get("value"));
				++resultCount;
			}
			Cursor cursor = new Cursor(nextStartDomainId, hasNext, resultCount);
			return new Data(jsonNodes, cursor);
		}
	}

	static class MongoDbDataDocument extends MongoDbDocument<JsonNode> {
		public MongoDbDataDocument(JsonNode valueObject) {
			super(valueObject, JsonNode.class);
		}


		public MongoDbDataDocument(Document document) {
			super(document, JsonNode.class);
		}


	}
}
