package org.jvalue.ods.db.couchdb.repositories;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.commons.couchdb.DbDocument;
import org.jvalue.commons.couchdb.DbDocumentAdaptable;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentNotFoundException;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.views.generic.TransformationFunction;

import java.util.List;


public final class TransformationFunctionRepository extends RepositoryAdapter<
	TransformationFunctionRepository.TransformationFunctionCouchDbRepository,
	TransformationFunctionRepository.TransformationFunctionDocument,
	TransformationFunction> implements GenericRepository<TransformationFunction> {

	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.transformationFunction != null";


	@Inject
	public TransformationFunctionRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new TransformationFunctionCouchDbRepository((CouchDbConnector) dbConnectorFactory.createConnector(databaseName, true)));
	}


	@View(name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc) }")
	static final class TransformationFunctionCouchDbRepository
		extends CouchDbRepositorySupport<TransformationFunctionRepository.TransformationFunctionDocument>
		implements DbDocumentAdaptable<TransformationFunctionDocument, TransformationFunction> {

		public TransformationFunctionCouchDbRepository(CouchDbConnector connector) {
			super(TransformationFunctionDocument.class, connector);
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id) }")
		public TransformationFunctionDocument findById(String viewId) {
			List<TransformationFunctionDocument> views = queryView("by_id", viewId);
			if (views.isEmpty()) throw new GenericDocumentNotFoundException(viewId);
			if (views.size() > 1)
				throw new IllegalStateException("found more than one chain for id " + viewId);
			return views.get(0);
		}


		@Override
		public TransformationFunctionDocument createDbDocument(TransformationFunction reference) {
			return new TransformationFunctionDocument(reference);
		}


		@Override
		public String getIdForValue(TransformationFunction value) {
			return value.getId();
		}

	}


	static final class TransformationFunctionDocument extends DbDocument<TransformationFunction> {

		@JsonCreator
		public TransformationFunctionDocument(
			@JsonProperty("value") TransformationFunction reference) {
			super(reference);
		}

	}
}
