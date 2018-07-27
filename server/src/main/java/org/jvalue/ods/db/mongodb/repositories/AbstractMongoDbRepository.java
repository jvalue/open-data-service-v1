package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jvalue.commons.EntityBase;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;

import java.util.List;

public abstract class AbstractMongoDbRepository<T extends EntityBase> implements GenericRepository<T>{

	public static final String DATABASE_NAME = "ods";
	final MongoDatabase database;
	protected MongoCollection<Document> collection;
	ObjectMapper mapper;

	public AbstractMongoDbRepository(DbConnectorFactory connectorFactory, String collectionName){
		this.database = (MongoDatabase) connectorFactory.createConnector(DATABASE_NAME,true);
		this.collection = database.getCollection(collectionName);
		this.mapper = new ObjectMapper();
	}
	@Override
	public abstract T findById(String Id);


	@Override
	public void add(T Value){
		String objectAsJsonString = null;
		try {
			objectAsJsonString = mapper.writeValueAsString(Value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Document parse = Document.parse(objectAsJsonString);
		parse.append("_id", Value.getId());
		collection.insertOne(parse);
	}


	@Override
	public void update(T value) {
		String objectAsJsonString = null;
		try {
			objectAsJsonString = mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Document parse = Document.parse(objectAsJsonString);
		Document searchQuery = new Document("_id", value.getId());
		collection.updateOne(searchQuery, parse);
	}

	@Override
	public void remove(T Value){
		Document searchQuery = new Document("_id", Value.getId());
		collection.deleteOne(searchQuery);
	}

	@Override
	public abstract List<T> getAll();
}
