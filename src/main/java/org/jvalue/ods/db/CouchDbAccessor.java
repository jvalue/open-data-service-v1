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

import java.util.LinkedList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class CouchDbAdapter.
 */
public class CouchDbAccessor implements DbAccessor {

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
	protected CouchDbAccessor(String databaseName) {
		if ((databaseName == null) || (databaseName.isEmpty())) {
			String errorMessage = "databaseName is null or empty";
			Logging.error(this.getClass(), errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		this.databaseName = databaseName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return isConnected;
	}

	
	private void createDbIfNotExist()
	{
		try {
			if (!dbInstance.checkIfDbExists(databaseName)) {
				dbInstance.createDatabase(databaseName);
			}
		} catch (Exception ex) {
			Logging.error(this.getClass(), ex.getMessage());
			System.err.println("CouchDb needs to be installed!\n"
				+ ex.getMessage());
			throw new DbException(ex);
		}
	}
	
	/**
	 * Check db state.
	 */
	private void checkDbState() {
		if (!isConnected) {
			String errorMessage = "The database is not connected";
			Logging.error(this.getClass(), errorMessage);
			throw new IllegalStateException(errorMessage);
		}
		
		createDbIfNotExist();

	}

	/**
	 * Connect.
	 */
	@Override
	public void connect() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		dbInstance = new StdCouchDbInstance(httpClient);
		createDbIfNotExist();

		db = new StdCouchDbConnector(databaseName, dbInstance);
		isConnected = true;
	}

	/**
	 * Gets the document.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param c
	 *            the c
	 * @param id
	 *            the id
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

		try {
			List<String> allDocs = db.getAllDocIds();
			if (allDocs.isEmpty()) {
				String errorMessage = "DB is empty.";
				Logging.error(this.getClass(), errorMessage);
				throw new DbException(errorMessage);
			}

			return allDocs.get(allDocs.size() - 1);
		} catch (Exception ex) {
			Logging.error(this.getClass(), ex.getMessage());
			throw new DbException(ex);
		}
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
		if (data == null) {
			String errorMessage = "data is null";
			Logging.error(this.getClass(), errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		checkDbState();

		db.create(data);
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
		if (data == null) {
			String errorMessage = "data is null";
			Logging.error(this.getClass(), errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		checkDbState();

		try {
			db.update(data);
		} catch (Exception ex) {
			throw new DbException(ex);
		}

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
		} catch (Exception ex) {
			Logging.error(this.getClass(), ex.getMessage());
			System.err.println("CouchDb needs to be installed!\n"
					+ ex.getMessage());
			throw new DbException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#getAllDocuments()
	 */
	@Override
	public List<JsonNode> getAllDocuments() {

		ViewQuery q = new ViewQuery().allDocs().includeDocs(true);
		ViewResult result = db.queryView(q);

		List<JsonNode> list = new LinkedList<>();

		for (Row r : result.getRows()) {
			list.add(r.getDocAsNode());
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#getNodeByName(java.lang.String)
	 */
	@Override
	public JsonNode getNodeByName(String name) {

		ViewQuery q = new ViewQuery().designDocId("_design/pegelonline")
				.viewName("getSingleStation").key(name).includeDocs(true);

		List<Row> l = db.queryView(q).getRows();

		if (l.isEmpty()) {
			Logging.error(this.getClass(),
					"Query not successful: !\n" + q.toString());
			System.err.println("Query not successful: !\n" + q.toString());
			throw new DbException("Unsuccessful query");
		}

		JsonNode ret = l.get(0).getDocAsNode();

		return ret;
	}

}
