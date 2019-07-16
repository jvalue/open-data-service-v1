package org.jvalue.ods;


import org.ektorp.CouchDbInstance;
import org.junit.After;
import org.junit.Before;
import org.jvalue.commons.couchdb.DbConnectorFactory;

import java.util.List;

public abstract class AbstractRepositoryTest {

	private static final String DB_PREFIX = "test";

	private CouchDbInstance couchDbInstance;


	@Before
	public final void createDatabase() {
		this.couchDbInstance = DbFactory.createCouchDbInstance();
		DbConnectorFactory dbConnectorFactory = new DbConnectorFactory(couchDbInstance, DB_PREFIX);
		doCreateDatabase(dbConnectorFactory);
	}


	protected abstract void doCreateDatabase(DbConnectorFactory dbConnectorFactory);


	@After
	public final void deleteDatabase() {
		List<String> databaseNames = couchDbInstance.getAllDatabases();
		for (String name : databaseNames) {
			if (name.startsWith(DB_PREFIX)) couchDbInstance.deleteDatabase(name);
		}
	}


}
