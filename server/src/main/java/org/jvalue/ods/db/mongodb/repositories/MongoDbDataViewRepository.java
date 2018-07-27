package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MongoDbDataViewRepository extends AbstractMongoDbRepository<CouchDbDataView>{

	@Inject
	public MongoDbDataViewRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName){
		super(dbConnectorFactory, databaseName);
	}

	@Override
	public CouchDbDataView findById(String Id) {
		Document findByIdQuery = new Document();
		findByIdQuery.put("_id", new ObjectId(Id));

		//should only return one
		Document dataSources = collection.find(findByIdQuery).first();

		CouchDbDataView couchDbDataView = null;
		try {
			assert dataSources != null;
			couchDbDataView = mapper.readValue(dataSources.toJson(), CouchDbDataView.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return couchDbDataView;
	}

	@Override
	public List<CouchDbDataView> getAll() {
		FindIterable<Document> documents = collection.find();
		List<CouchDbDataView> CouchDbDataView = new ArrayList<>();
		try {
			for (Document doc : documents){
				CouchDbDataView documentAsObject = null;
				try {
					documentAsObject = mapper.readValue(doc.toJson(), CouchDbDataView.class);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				CouchDbDataView.add(documentAsObject);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CouchDbDataView;
	}
}
