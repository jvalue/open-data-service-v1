package org.jvalue.ods.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ods.api.sources.DataSource;

import java.util.List;

public final class DataSourceRepository extends RepositoryAdapter<
		DataSourceRepository.DataSourceCouchDbRepository,
		DataSourceRepository.DataSourceDocument,
		DataSource> {

	static final String DATABASE_NAME = "dataSources";
	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.domainIdKey != null";

	@Inject
	DataSourceRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(new DataSourceCouchDbRepository(connector));
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc)}")
	static final class DataSourceCouchDbRepository
			extends CouchDbRepositorySupport<DataSourceRepository.DataSourceDocument>
			implements DbDocumentAdaptable<DataSourceRepository.DataSourceDocument, DataSource> {


		public DataSourceCouchDbRepository(CouchDbConnector connector) {
			super(DataSourceDocument.class, connector);
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id)}")
		public DataSourceDocument findById(String sourceId) {
			List<DataSourceDocument> sources = queryView("by_id", sourceId);
			if (sources.isEmpty()) throw new DocumentNotFoundException(sourceId);
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
