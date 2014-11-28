package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.data.DataSource;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.sourceId && doc.domainIdKey ) emit( null, doc)}")
public final class DataSourceRepository extends CouchDbRepositorySupport<DataSource> {

	static final String DATABASE_NAME = "dataSources";

	@Inject
	DataSourceRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(DataSource.class, connector);
		initStandardDesignDocument();
	}


	@GenerateView
	public List<DataSource> findBySourceId(String sourceId) {
		return queryView("by_sourceId", sourceId);
	}


}
