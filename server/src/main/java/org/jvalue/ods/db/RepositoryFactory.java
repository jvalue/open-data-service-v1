/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;

public interface RepositoryFactory {

	public DataViewRepository createDataViewRepository(String databaseName);

	public DataRepository createSourceDataRepository(String databaseName, JsonPointer domainIdKey);
	public ProcessorChainReferenceRepository createFilterChainReferenceRepository(String databaseName);
	public NotificationClientRepository createNotificationClientRepository(String databaseName);
	public PluginMetaDataRepository createPluginMetaDataRepository(String databaseName);

}
