/*
 * 
 */
package org.jvalue.ods.inserter;


import java.util.List;

import org.ektorp.*;
import org.ektorp.http.*;
import org.ektorp.impl.*;
import org.ektorp.support.CouchDbDocument;


/**
 * The Class CouchDbInserter.
 */
public class CouchDbInserter implements Inserter {
	
	/** The db. */
	private CouchDbConnector db;
	
	/** The data. */
	private List<CouchDbDocument> data;

	/**
	 * Instantiates a new pegel online db inserter.
	 *
	 * @param databaseName the database name
	 * @param data the data
	 */
	public CouchDbInserter(String databaseName, List<CouchDbDocument> data) {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		if (!dbInstance.checkIfDbExists(databaseName))
			dbInstance.createDatabase(databaseName);		
		this.db = new StdCouchDbConnector(databaseName, dbInstance);
		
		this.data = data;
	}


	/* (non-Javadoc)
	 * @see org.jvalue.ods.inserter.Inserter#insert()
	 */
	public void insert() {
		// insert each document into couchdb
		for (CouchDbDocument s : this.data) {	
			db.create(s);
			System.out.println("Insert into db: " + s.getId());
		}
	}
	
}
