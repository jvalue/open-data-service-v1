package org.jvalue.ods.db.mongodb.repositories;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.bson.Document;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.value.commons.mongodb.AbstractMongoDbRepository;
import org.value.commons.mongodb.MongoDbDocument;
import org.value.commons.mongodb.MongoDbDocumentAdaptable;
import org.value.commons.mongodb.MongoDbRepositoryAdapter;

public class MongoDbDataViewRepository extends AbstractMongoDbRepository<CouchDbDataView> {

	private static final String COLLECTION_NAME = "dataViewCollection";


	@Inject
	public MongoDbDataViewRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(dbConnectorFactory, databaseName, COLLECTION_NAME, CouchDbDataView.class);
	}


	@Override
	protected String getValueId(CouchDbDataView Value) {
		return Value.getId();
	}
}
