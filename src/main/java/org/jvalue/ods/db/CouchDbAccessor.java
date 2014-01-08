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
package org.jvalue.ods.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

/**
 * The Class CouchDbAccessor.
 * abstracts the connection to db
 */
public abstract class CouchDbAccessor {

	/** The db. */
	protected CouchDbConnector db;

	/**
	 * Instantiates a new couch db accessor.
	 * 
	 * @param databaseName
	 *            the database name
	 */
	public CouchDbAccessor(String databaseName) {
		if (databaseName == null) {
			throw new IllegalArgumentException("databaseName is null");
		}
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);

		try {
			if (!dbInstance.checkIfDbExists(databaseName)) {
				dbInstance.createDatabase(databaseName);
			}
		} catch (DbAccessException ex) {
			System.err.println("CouchDb needs to be installed!\n"
					+ ex.getMessage());
			throw ex;
		}

		this.db = new StdCouchDbConnector(databaseName, dbInstance);
	}
	
}
