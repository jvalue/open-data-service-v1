package org.jvalue.ods.db.couchdb;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.commons.couchdb.DbDocument;
import org.jvalue.commons.couchdb.DbDocumentAdaptable;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.commons.db.IRepository;

import java.util.List;


public final class DataViewRepository extends RepositoryAdapter<
		DataViewRepository.DataViewCouchDbRepository,
		DataViewRepository.DataViewDocument,
	CouchDbDataView> implements IRepository<CouchDbDataView>{

	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.mapFunction != null";

	@Inject
	public DataViewRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new DataViewCouchDbRepository((CouchDbConnector) dbConnectorFactory.createConnector(databaseName, true)));
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc) }")
	static final class DataViewCouchDbRepository
			extends CouchDbRepositorySupport<DataViewRepository.DataViewDocument>
			implements DbDocumentAdaptable<DataViewDocument, CouchDbDataView> {


		public DataViewCouchDbRepository(CouchDbConnector connector) {
			super(DataViewDocument.class, connector);
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id) }")
		public DataViewDocument findById(String viewId) {
			List<DataViewDocument> views = queryView("by_id", viewId);
			if (views.isEmpty()) throw new DocumentNotFoundException(viewId);
			if (views.size() > 1)
				throw new IllegalStateException("found more than one view for id " + viewId);
			return views.get(0);
		}


		@Override
		public DataViewDocument createDbDocument(CouchDbDataView view) {
			return new DataViewDocument(view);
		}


		@Override
		public String getIdForValue(CouchDbDataView view) {
			return view.getId();
		}

	}


	static final class DataViewDocument extends DbDocument<CouchDbDataView> {

		@JsonCreator
		public DataViewDocument(
				@JsonProperty("value") CouchDbDataView dataView) {
			super(dataView);
		}

	}

}
