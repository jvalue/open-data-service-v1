package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.notifications.Client;

import java.util.List;

public class MongoDbNotificationClientRepository extends AbstractMongoDbRepository<Client>{
	public MongoDbNotificationClientRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName);
	}

	@Override
	public Client findById(String Id) {
//		Document findByIdQuery = new Document();
//		findByIdQuery.put("_id", new ObjectId(Id));
//
//		//should only return one
//		Document dataSources = collection.find(findByIdQuery).first();
//
//		Client clientDocument = null;
//		try {
//			assert dataSources != null;
//			clientDocument = mapper.readValue(dataSources.toJson(), Client.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}


	@Override
	public List<Client> getAll() {
//		FindIterable<Document> documents = collection.find();
//
//		List<ClientDocument> clientList = new ArrayList<>();
//		try {
//			for (Document doc : documents){
//				ClientDocument clientDoc = mapper.readValue(doc.toJson(), ClientDocument.class);
//				clientList.add(clientDoc);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}
}
