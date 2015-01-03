package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.data.DataView;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.viewId && doc.mapFunction) emit(null, doc) }")
public final class DataViewRepository extends CouchDbRepositorySupport<DataView> {

	@Inject
	DataViewRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(DataView.class, couchDbInstance.createConnector(databaseName, true));
		initStandardDesignDocument();
	}


	@GenerateView
	public DataView findByViewId(String viewId) {
		List<DataView> views = queryView("by_viewId", viewId);
		if (views.isEmpty()) throw new DocumentNotFoundException(viewId);
		if (views.size() > 1) throw new IllegalStateException("found more than one view for id " + viewId);
		return views.get(0);
	}

}
