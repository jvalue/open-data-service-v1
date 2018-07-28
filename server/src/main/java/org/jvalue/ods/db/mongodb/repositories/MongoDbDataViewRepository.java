package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;

public class MongoDbDataViewRepository extends AbstractMongoDbRepository<CouchDbDataView> {

	private static final String COLLECTION_NAME = "dataViewCollection";


	@Inject
	public MongoDbDataViewRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, CouchDbDataView.class);
	}
}
