package org.jvalue.ods.db;


import com.fasterxml.jackson.core.JsonPointer;

public interface DbFactory {

	public SourceDataRepository createSourceDataRepository(String databasename, JsonPointer domainIdKey);

}
