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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.schema.MapComplexValueType;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.schema.SchemaManager;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class CouchDbAdapter.
 */
public class CouchDbAccessor implements DbAccessor<JsonNode> {

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

	/**
	 * Creates the db if not exist.
	 */
	private void createDbIfNotExist() {
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
		if (id.length() == 0) {
			throw new IllegalArgumentException("id is empty");
		}

		checkDbState();

		T ret = null;

		try {
			ret = db.get(c, id);
		} catch (DocumentNotFoundException e) {
			throw new DbException(e);
		}

		return ret;
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

		try {
			db.create(data);
		} catch (Exception ex) {
			throw new DbException(ex);
		}
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

		checkDbState();

		ViewQuery q = new ViewQuery().allDocs().includeDocs(true);
		ViewResult result = db.queryView(q);

		List<JsonNode> list = new LinkedList<JsonNode>();

		for (Row r : result.getRows()) {
			list.add(r.getDocAsNode());
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#executeDocumentQuery(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<JsonNode> executeDocumentQuery(String designDocId,
			String viewName, String key) {

		if (designDocId == null) {
			throw new IllegalArgumentException("designDocId is null");
		}
		if (viewName == null) {
			throw new IllegalArgumentException("viewName is null");
		}
		if (designDocId.length() == 0) {
			throw new IllegalArgumentException("designDocId is empty");
		}
		if (viewName.length() == 0) {
			throw new IllegalArgumentException("viewName is empty");
		}

		checkDbState();

		ViewQuery q = new ViewQuery().designDocId(designDocId)
				.viewName(viewName).key(key);

		List<JsonNode> ret = new LinkedList<JsonNode>();
		ViewResult vq = db.queryView(q);
		List<Row> l = vq.getRows();

		if (l.isEmpty()) {
			Logging.info(this.getClass(), "Empty result list.\n" + q.toString());
		} else {
			for (Row r : l) {
				ret.add(r.getValueAsNode());
			}

		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#executeBulk(java.util.Collection)
	 */
	@Override
	public void executeBulk(Collection<MapObject> objects, MapComplexValueType schema) {

		if (objects == null) {
			throw new IllegalArgumentException("objects is null");
		}

		checkDbState();

		if (schema != null) {

			for (MapObject mv : objects) {

				if (!SchemaManager.validateGenericValusFitsSchema(mv, schema)) {
					Logging.error(this.getClass(),
							"Data does not fit DB schema.");
					System.err.println("Data does not fit DB schema.");
					return;
				}

			}

			for (MapObject mv : objects) {

				MapComplexValueType ms = null;
				try {
					ms = (MapComplexValueType) schema.getMap().get("objectType");
					String s = (String) ms.getMap().keySet().toArray()[0];

					mv.getMap().put("dataType", new BaseObject(s));
				} catch (Exception ex) {
					Logging.error(this.getClass(), ex.getMessage()
							+ "Schema does not contain objectType.");
					System.err.println(ex.getMessage()
							+ "Schema does not contain objectType.");
					throw ex;
				}

			}

			insert(schema);

		}
		try {

			db.executeBulk(objects);
		} catch (Exception ex) {
			Logging.error(this.getClass(), ex.getMessage());
			System.err.println("Bulk operation on db failed!\n"
					+ ex.getMessage());
			throw new DbException(ex);
		}
	}

}
