package org.jvalue.ods.db;


import org.jvalue.ods.utils.JsonPropertyKey;

public interface DbFactory {

	public SourceDataRepository createSourceDataRepository(String databasename, JsonPropertyKey domainIdKey);

}
