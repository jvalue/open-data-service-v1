/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.inserter;


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

	/** The data to insert. */
	private CouchDbDocument data;

	/**
	 * Instantiates a new pegel online db inserter. Database will be created if does not exist yet
	 * 
	 * @param databaseName
	 *            the database name
	 * @param data
	 *            the data to insert
	 */
	public CouchDbInserter(String databaseName, CouchDbDocument data) {
		if (databaseName == null)
			throw new IllegalArgumentException("databaseName is null");
		if (data == null)
			throw new IllegalArgumentException("data is null");
		
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
		db.create(data);
	}

}
