package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;

public interface DbFactory {

	public DataViewRepository createDataViewRepository(String databaseName);

	public DataRepository createSourceDataRepository(String databaseName, JsonPointer domainIdKey);
	public ProcessorChainReferenceRepository createFilterChainReferenceRepository(String databaseName);
	public NotificationClientRepository createNotificationClientRepository(String databaseName);
	public PluginMetaDataRepository createPluginMetaDataRepository(String databaseName);

}
