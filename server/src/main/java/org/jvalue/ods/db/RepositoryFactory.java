package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.name.Named;
import org.ektorp.CouchDbConnector;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;

public interface RepositoryFactory {
	static final String
		NAME_DATA_VIEW_REPOSITORY = "DataViewRepository",
		NAME_DATA_SOURCE_REPOSITORY = "SourceDataRepository",
		NAME_DATA_REPOSITORY = "DataRepository",
		NAME_FILTER_CHAIN_REF_REPOSITORY = "FilterChainRepository",
		NAME_NOTIFICATION_CLIENT_REPOSITORY = "NotifictionClientRepository",
		NAME_PLUGIN_META_DATA_REPOSITORY = "PluginMetaDataRepository";

	@Named(NAME_DATA_VIEW_REPOSITORY)
	public IRepository<DataView> createDataViewRepository(String databaseName);

	@Named(NAME_DATA_SOURCE_REPOSITORY)
	public IRepository<DataSource> createDataSourceRepository(CouchDbConnector connector);

	@Named(NAME_DATA_REPOSITORY)
	public IDataRepository<JsonNode> createSourceDataRepository(String databaseName, JsonPointer domainIdKey);

	@Named(NAME_FILTER_CHAIN_REF_REPOSITORY)
	public IRepository<ProcessorReferenceChain> createFilterChainReferenceRepository(String databaseName);

	@Named(NAME_NOTIFICATION_CLIENT_REPOSITORY)
	public IRepository<Client> createNotificationClientRepository(String databaseName);

	@Named(NAME_PLUGIN_META_DATA_REPOSITORY)
	public IRepository<PluginMetaData> createPluginMetaDataRepository(String databaseName);
}
