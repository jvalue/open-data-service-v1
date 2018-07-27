package org.jvalue.ods.db.mongodb.repositories;

import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.sources.DataSource;

import java.util.List;

public class MongoDbDataSourceRepository extends AbstractMongoDbRepository<DataSource>{

	public static final String COLLECTION_NAME = "dataSource";

	public MongoDbDataSourceRepository(DbConnectorFactory connectorFactory){
		super(connectorFactory, COLLECTION_NAME);
	}


	@Override
	public DataSource findById(String Id) {
//		Document findByIdQuery = new Document();
//		findByIdQuery.put("_id", new ObjectId(Id));
//
//		//should only return one
//		Document dataSources = collection.find(findByIdQuery).first();
//
//		DataSource dataSource = null;
//		try {
//			dataSource = mapper.readValue(dataSources.toJson(), DataSource.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}


	@Override
	public List<DataSource> getAll() {
		return null;
	}


}
