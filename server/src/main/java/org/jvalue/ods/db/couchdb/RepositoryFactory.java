package org.jvalue.ods.db.couchdb;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.name.Named;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;

public interface RepositoryFactory {
	static final String
		NAME_DATA_VIEW_REPOSITORY = "MongoDbDataViewRepository",
		NAME_DATA_SOURCE_REPOSITORY = "SourceDataRepository",
		NAME_DATA_REPOSITORY = "DataRepository",
		NAME_FILTER_CHAIN_REF_REPOSITORY = "FilterChainRepository",
		NAME_NOTIFICATION_CLIENT_REPOSITORY = "NotifictionClientRepository",
		NAME_PLUGIN_META_DATA_REPOSITORY = "PluginMetaDataRepository";

	@Named(NAME_DATA_VIEW_REPOSITORY)
	public GenericRepository<CouchDbDataView> createDataViewRepository(String databaseName);

	@Named(NAME_DATA_REPOSITORY)
	public GenericDataRepository<CouchDbDataView, JsonNode> createSourceDataRepository(String databaseName, JsonPointer domainIdKey);

	@Named(NAME_FILTER_CHAIN_REF_REPOSITORY)
	public GenericRepository<ProcessorReferenceChain> createFilterChainReferenceRepository(String databaseName);

	@Named(NAME_NOTIFICATION_CLIENT_REPOSITORY)
	public GenericRepository<Client> createNotificationClientRepository(String databaseName);

	@Named(NAME_PLUGIN_META_DATA_REPOSITORY)
	public GenericRepository<PluginMetaData> createPluginMetaDataRepository(String databaseName);
}
