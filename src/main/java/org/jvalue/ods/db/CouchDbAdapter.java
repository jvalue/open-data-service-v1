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

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbAccessException;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.jvalue.ods.logger.Logging;

/**
 * The Class CouchDbAdapter.
 */
public class CouchDbAdapter implements DbAdapter {

	/** The db. */
	private CouchDbConnector db;

	/** The db instance. */
	private CouchDbInstance dbInstance;

	/** The database name. */
	private String databaseName;

	/** The is connected. */
	private boolean isConnected = false;
	
	/**
	 * Instantiates a new couch db adapter.
	 * 
	 * @param databaseName
	 *            the database name
	 */
	protected CouchDbAdapter(String databaseName) {
		if ((databaseName == null) || (databaseName.isEmpty())) {
			throw new IllegalArgumentException("databaseName is null or empty");
		}

		this.databaseName = databaseName;
	}
	
	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#isConnected()
	 */
	@Override
	public boolean isConnected()
	{		
		return isConnected;
	}
	
	/**
	 * Check db state.
	 */
	private void checkDbState() {
        if (!isConnected) {
            throw new IllegalStateException("The database is not connected");
        }
    }

	/**
	 * Connect.
	 */
	@Override
	public void connect() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		dbInstance = new StdCouchDbInstance(httpClient);

		try {
			if (!dbInstance.checkIfDbExists(databaseName)) {
				dbInstance.createDatabase(databaseName);
			}
		} catch (DbAccessException ex) {
			Logging.error(this.getClass(), ex.getMessage());
			System.err.println("CouchDb needs to be installed!\n"
					+ ex.getMessage());
			throw ex;
		}

		db = new StdCouchDbConnector(databaseName, dbInstance);
		
		isConnected = true;
	}

	/**
	 * Gets the document.
	 *
	 * @param <T> the generic type
	 * @param c the c
	 * @param id the id
	 * @return the document
	 */
	@Override
	public <T> T getDocument(Class<T> c, String id) {
		if (c == null) {
			throw new IllegalArgumentException("c is null");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is null");
		}
		
		checkDbState();
			
		return db.get(c, id);
	}

	/**
	 * Gets the last document id.
	 * 
	 * @return the last document id
	 */
	@Override
	public String getLastDocumentId() {
		checkDbState();
		
		List<String> allDocs = db.getAllDocIds();
		if (allDocs.isEmpty())
			throw new DbAccessException("DB is empty.");
		else
			return allDocs.get(allDocs.size() - 1);
	}

	/**
	 * Insert.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 */
	@Override
	public void insert(Object data) {
		if (data == null)
			throw new IllegalArgumentException("data is null");

		checkDbState();
		
		db.create(data);

		//return data.getId();
	}

	/**
	 * Update.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 */
	@Override
	public void update(Object data) {
		if (data == null)
			throw new IllegalArgumentException("data is null");

		checkDbState();
		
		db.update(data);
	}

	/**
	 * Delete database.
	 */
	@Override
	public void deleteDatabase() {
		checkDbState();
		
		String databaseName = db.getDatabaseName();

		try {
			if (dbInstance.checkIfDbExists(databaseName)) {
				dbInstance.deleteDatabase(databaseName);
			}
		} catch (DbAccessException ex) {
			Logging.error(this.getClass(), ex.getMessage());
			System.err.println("CouchDb needs to be installed!\n"
					+ ex.getMessage());
			throw ex;
		}
	}

}
