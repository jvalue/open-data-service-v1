package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;

public interface DbFactory {

	public DataRepository createSourceDataRepository(String databaseName, JsonPointer domainIdKey);
	public FilterChainReferenceRepository createFilterChainReferenceRepository(String databaseName);
	public DataViewRepository createDataViewRepository(String databaseName);
	public NotificationClientRepository createNotificationClientRepository(String databaseName);
	public PluginMetaDataRepository createPluginMetaDataRepository(String databaseName);

}
