package org.jvalue.ods.db;


public interface DbFactory {

	public SourceDataRepository createSourceDataRepository(String databasename);

}
