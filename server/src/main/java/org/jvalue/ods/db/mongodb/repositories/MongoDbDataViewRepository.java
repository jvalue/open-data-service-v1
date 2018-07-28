package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;

public class MongoDbDataViewRepository extends AbstractMongoDbRepository<CouchDbDataView> {

	@Inject
	public MongoDbDataViewRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName);
	}


	@Override
	protected Class<?> getEntityType() {
		return CouchDbDataView.class;
	}


}
