/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.DbDocument;
import org.jvalue.commons.couchdb.DbDocumentAdaptable;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.ods.api.views.DataView;

import java.util.List;


public final class DataViewRepository extends RepositoryAdapter<
		DataViewRepository.DataViewCouchDbRepository,
		DataViewRepository.DataViewDocument,
		DataView> {

	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.mapFunction != null";

	@Inject
	DataViewRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new DataViewCouchDbRepository(dbConnectorFactory.createConnector(databaseName, true)));
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc) }")
	static final class DataViewCouchDbRepository
			extends CouchDbRepositorySupport<DataViewRepository.DataViewDocument>
			implements DbDocumentAdaptable<DataViewDocument, DataView> {


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
