package org.jvalue.ods.db.couchdb.repositories;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.commons.couchdb.DbDocument;
import org.jvalue.commons.couchdb.DbDocumentAdaptable;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentNotFoundException;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;

import java.util.List;

public final class DataSourceRepository extends RepositoryAdapter<
	DataSourceRepository.DataSourceCouchDbRepository,
	DataSourceRepository.DataSourceDocument,
	DataSource> implements GenericRepository<DataSource> {

	public static final String DATABASE_NAME = "dataSources";
	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.domainIdKey != null";


	public DataSourceRepository(DbConnectorFactory connectorFactory) {
		super(new DataSourceCouchDbRepository((CouchDbConnector) connectorFactory.createConnector(DATABASE_NAME, true)));
	}


	@View(name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc)}")
	static final class DataSourceCouchDbRepository
		extends CouchDbRepositorySupport<DataSourceRepository.DataSourceDocument>
		implements DbDocumentAdaptable<DataSourceDocument, DataSource> {


		public DataSourceCouchDbRepository(CouchDbConnector connector) {
			super(DataSourceDocument.class, connector);
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id)}")
		public DataSourceDocument findById(String sourceId) {
			List<DataSourceDocument> sources = queryView("by_id", sourceId);
			if (sources.isEmpty()) throw new GenericDocumentNotFoundException(sourceId);
			if (sources.size() > 1)
				throw new IllegalStateException("found more than one source for id " + sourceId);
			return sources.get(0);
		}


		@Override
		public DataSourceDocument createDbDocument(DataSource source) {
			return new DataSourceDocument(source);
		}


		@Override
		public String getIdForValue(DataSource source) {
			return source.getId();
		}
	}


	static final class DataSourceDocument extends DbDocument<DataSource> {

		@JsonCreator
		public DataSourceDocument(
			@JsonProperty("value") DataSource dataSource) {
			super(dataSource);
		}

	}

}
