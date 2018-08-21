package org.jvalue.ods.db.couchdb.repositories;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.ektorp.Attachment;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.commons.couchdb.DbDocument;
import org.jvalue.commons.couchdb.DbDocumentAdaptable;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.db.DbConnectorFactory;
import org.jvalue.commons.db.GenericDocumentNotFoundException;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.db.generic.GenericPluginMetaDataRepository;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


public final class PluginMetaDataRepository extends RepositoryAdapter<
	PluginMetaDataRepository.PluginMetaDataCouchDbRepository,
	PluginMetaDataRepository.PluginMetaDataDocument,
	PluginMetaData> implements GenericPluginMetaDataRepository<PluginMetaData> {

	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.author != null";


	@Inject
	public PluginMetaDataRepository(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		super(new PluginMetaDataCouchDbRepository((CouchDbConnector) dbConnectorFactory.createConnector(databaseName, true)));
	}


	public void addAttachment(PluginMetaData metaData, InputStream stream, String contentType) {
		repository.addAttachment(repository.findById(metaData.getId()), stream, contentType);
	}


	public InputStream getAttachment(PluginMetaData metaData) {
		return repository.getAttachment(repository.findById(metaData.getId()));
	}


	@View(name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit( null, doc)}")
	static final class PluginMetaDataCouchDbRepository
		extends CouchDbRepositorySupport<PluginMetaDataRepository.PluginMetaDataDocument>
		implements DbDocumentAdaptable<PluginMetaDataDocument, PluginMetaData> {

		private final CouchDbConnector connector;


		PluginMetaDataCouchDbRepository(CouchDbConnector connector) {
			super(PluginMetaDataDocument.class, connector);
			this.connector = connector;
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id) }")
		public PluginMetaDataDocument findById(String pluginId) {
			List<PluginMetaDataDocument> plugins = queryView("by_id", pluginId);
			if (plugins.isEmpty()) throw new GenericDocumentNotFoundException(pluginId);
			if (plugins.size() > 1) throw new IllegalStateException("found more than one plugin for id " + pluginId);
			return plugins.get(0);
		}


		@Override
		public PluginMetaDataDocument createDbDocument(PluginMetaData metaData) {
			return new PluginMetaDataDocument(metaData);
		}


		@Override
		public String getIdForValue(PluginMetaData metaData) {
			return metaData.getId();
		}


		public void addAttachment(PluginMetaDataDocument metaData, InputStream stream, String contentType) {
			AttachmentInputStream attachmentInputStream = new AttachmentInputStream(metaData.getValue().getId(), stream, contentType);
			connector.createAttachment(metaData.getId(), metaData.getRevision(), attachmentInputStream);
		}


		public InputStream getAttachment(PluginMetaDataDocument metaData) {
			Map<String, Attachment> attachments = metaData.getAttachments();
			Attachment attachment = attachments.get(metaData.getValue().getId());
			if (attachment == null)
				throw new IllegalStateException("did not find any attachments for plugin " + metaData.getValue().getId());
			return connector.getAttachment(metaData.getId(), metaData.getValue().getId());
		}

	}


	static final class PluginMetaDataDocument extends DbDocument<PluginMetaData> {

		@JsonCreator
		public PluginMetaDataDocument(
			@JsonProperty("value") PluginMetaData metaData) {
			super(metaData);
		}

	}
}
