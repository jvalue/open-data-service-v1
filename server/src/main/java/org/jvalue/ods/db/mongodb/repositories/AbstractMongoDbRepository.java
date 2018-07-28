package org.jvalue.ods.db.mongodb.repositories;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.jvalue.commons.EntityBase;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Log;
import org.value.commons.mongodb.MongoDocumentNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


public abstract class AbstractMongoDbRepository<T extends EntityBase> implements GenericRepository<T> {

	private MongoCollection<Document> collection;
	private Class<T> type;


	public AbstractMongoDbRepository(DbConnectorFactory connectorFactory, String databaseName, String collectionName, Class<T> type) {
		MongoDatabase database = (MongoDatabase) connectorFactory.createConnector(databaseName, true);
		this.collection = database.getCollection(collectionName);
		this.type = type;
	}


	@Override
	public T findById(String Id) {
		//should only return one
		Document document = findDocumentById(Id);
		return deserializeDocument(document);
	}

	private Document findDocumentById(String Id){
		//should only return one
		Document document = collection.find(eq("_id", Id)).first();
		if (document == null) {
			throw new MongoDocumentNotFoundException();
		}
		return document;
	}

	private T deserializeDocument(Document document) {
		//remove first id field
		T entity;
		entity = new Gson().fromJson(document.toJson(), type);
		return entity;
	}


	@Override
	public void add(T Value) {
		String objectAsJsonString;

		try {
			objectAsJsonString = new Gson().toJson(Value);
			Document parse = Document.parse(objectAsJsonString);
			collection.insertOne(parse);
		} catch (Exception e) {
			Log.info(e.getMessage());
		}
	}


	@Override
	public void update(T value) {
		String objectAsJsonString;

		try {
			objectAsJsonString = new Gson().toJson(value);
			Document parse = Document.parse(objectAsJsonString);
			collection.replaceOne(Filters.eq("_id", value.getId()), parse);
		} catch (Exception e) {
			Log.info(e.getMessage());
		}
	}


	@Override
	public void remove(T Value) {
		collection.deleteOne(Filters.eq("_id", Value.getId()));
	}


	@Override
	public List<T> getAll() {
		FindIterable<Document> documents = collection.find();
		List<T> entityList = new ArrayList<>();
		for (Document doc : documents) {
			T documentAsObject;
			documentAsObject = deserializeDocument(doc);
			entityList.add(documentAsObject);
		}
		return entityList;
	}
}
