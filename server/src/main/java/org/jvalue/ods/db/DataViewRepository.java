package org.jvalue.ods.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ods.api.views.DataView;

import java.util.List;


public final class DataViewRepository extends RepositoryAdapter<
		DataViewRepository.DataViewCouchDbRepository,
		DataViewRepository.DataViewDocument,
		DataView> {

	@Inject
	DataViewRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(new DataViewCouchDbRepository(couchDbInstance, databaseName));
	}


	@View( name = "all", map = "function(doc) { if (doc.value.id && doc.value.mapFunction) emit(null, doc) }")
	static final class DataViewCouchDbRepository
			extends CouchDbRepositorySupport<DataViewRepository.DataViewDocument>
			implements DbDocumentAdaptable<DataViewRepository.DataViewDocument, DataView> {


		public DataViewCouchDbRepository(CouchDbInstance couchDbInstance, String databaseName) {
			super(DataViewDocument.class, couchDbInstance.createConnector(databaseName, true));
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (doc.value.id && doc.value.mapFunction) emit(doc.value.id, doc._id) }")
		public DataViewDocument findById(String viewId) {
			List<DataViewDocument> views = queryView("by_id", viewId);
			if (views.isEmpty()) throw new DocumentNotFoundException(viewId);
			if (views.size() > 1)
				throw new IllegalStateException("found more than one view for id " + viewId);
			return views.get(0);
		}


		@Override
		public DataViewDocument createDbDocument(DataView view) {
			return new DataViewDocument(view);
		}


		@Override
		public String getIdForValue(DataView view) {
			return view.getId();
		}

	}


	static final class DataViewDocument extends DbDocument<DataView> {

		@JsonCreator
		public DataViewDocument(
				@JsonProperty("value") DataView dataView) {
			super(dataView);
		}

	}

}
