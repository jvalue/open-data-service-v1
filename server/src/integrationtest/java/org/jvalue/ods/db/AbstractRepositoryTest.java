package org.jvalue.ods.db;


import org.ektorp.CouchDbInstance;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractRepositoryTest {

	private final String databaseName;
	private final CouchDbInstance couchdbInstance;

	public AbstractRepositoryTest(String databaseName) {
		this.databaseName = databaseName;
		this.couchdbInstance = DbFactory2.createCouchDbInstance();
	}


	@Before
	public final void createDatabase() {
		doCreateDatabase(couchdbInstance, databaseName);
	}


	protected abstract void doCreateDatabase(CouchDbInstance couchdbInstance, String databaseName);


	@After
	public final void deleteDatabase() {
		couchdbInstance.deleteDatabase(databaseName);
	}


}
