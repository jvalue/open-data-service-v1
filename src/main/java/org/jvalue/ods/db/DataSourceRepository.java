package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbInstance;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.data.DataSource;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.sourceId && doc.domainIdKey ) emit( null, doc)}")
public final class DataSourceRepository extends CouchDbRepositorySupport<DataSource> {

	@Inject
	DataSourceRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(DataSource.class, couchDbInstance.createConnector(databaseName, true));
		initStandardDesignDocument();
	}


	@GenerateView
	public List<DataSource> findBySourceId(String sourceId) {
		return queryView("by_sourceId", sourceId);
	}


}
