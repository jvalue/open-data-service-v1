package org.jvalue.ods.db;


import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.Attachment;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ods.filter.plugin.PluginMetaData;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@View( name = "all", map = "function(doc) { if (doc.pluginId) emit( null, doc)}")
public final class PluginMetaDataRepository extends CouchDbRepositorySupport<PluginMetaData> {

	private final CouchDbConnector connector;

	@Inject
	PluginMetaDataRepository(CouchDbInstance couchDbInstance, @Assisted String databaseName) {
		super(PluginMetaData.class, couchDbInstance.createConnector(databaseName, true));
		this.connector = couchDbInstance.createConnector(databaseName, true);
		initStandardDesignDocument();
	}


	@GenerateView
	public PluginMetaData findByPluginId(String pluginId) {
		List<PluginMetaData> plugins = queryView("by_pluginId", pluginId);
		if (plugins.isEmpty()) throw new DocumentNotFoundException(pluginId);
		if (plugins.size() > 1) throw new IllegalStateException("found more than one plugin for id " + pluginId);
		return plugins.get(0);
	}


	public void addAttachment(PluginMetaData metaData, InputStream stream, String contentType) {
		AttachmentInputStream attachmentInputStream = new AttachmentInputStream(metaData.getPluginId(), stream, contentType);
		connector.createAttachment(metaData.getId(), metaData.getRevision(), attachmentInputStream);
	}


	public InputStream getAttachment(PluginMetaData metaData) {
		Map<String, Attachment> attachments = metaData.getAttachments();
		Attachment attachment = attachments.get(metaData.getPluginId());
		if (attachment == null) throw new IllegalStateException("did not find any attachments for plugin " + metaData.getPluginId());
		return connector.getAttachment(metaData.getId(), metaData.getPluginId());
	}

}
