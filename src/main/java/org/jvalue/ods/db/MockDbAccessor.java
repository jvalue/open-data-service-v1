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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.db.exception.DbException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class MockDbAdapter.
 */
public class MockDbAccessor implements DbAccessor {

	/** The list. */
	List<SimpleEntry<String, Object>> list = new ArrayList<SimpleEntry<String, Object>>();

	/** The is connected. */
	private boolean isConnected = false;

	/**
	 * Instantiates a new mock db adapter.
	 * 
	 * @param databaseName
	 *            the database name
	 */
	public MockDbAccessor(String databaseName) {
		if ((databaseName == null) || (databaseName.isEmpty()))
			throw new IllegalArgumentException();
	}

	/**
	 * Check db state.
	 */
	private void checkDbState() {
		if (!isConnected) {
			throw new IllegalStateException("The database is not connected");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#getDocument(java.lang.Class,
	 * java.lang.String)
	 */
	@Override
	public <T> T getDocument(Class<T> c, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#getLastDocumentId()
	 */
	@Override
	public String getLastDocumentId() {
		if (list.size() == 0)
			throw new DbException("db is empty");
		else
			return list.get(list.size() - 1).getKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#insert(java.lang.Object)
	 */
	@Override
	public <T> void insert(T data) {
		if (data == null)
			throw new IllegalArgumentException("data is null");

		checkDbState();

		for (SimpleEntry<String, Object> s : list) {
			if (s.getKey().equals(data.toString()))
				throw new IllegalArgumentException();
		}

		if (data instanceof CouchDbDocument)
			((CouchDbDocument) data).setId(data.toString());

		list.add(new SimpleEntry<String, Object>(data.toString(), data));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#update(java.lang.Object)
	 */
	@Override
	public void update(Object data) {
		if (data == null)
			throw new IllegalArgumentException("data is null");

		checkDbState();

		if (data instanceof CouchDbDocument) {
			if (((CouchDbDocument) data).getRevision() == "invalidRevision")
				throw new DbException("invalid revision");

			((CouchDbDocument) data).setRevision(((CouchDbDocument) data)
					.getRevision() + "2");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#deleteDatabase()
	 */
	@Override
	public void deleteDatabase() {
		list.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#connect()
	 */
	@Override
	public void connect() {
		isConnected = true;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#getAllDocuments()
	 */
	@Override
	public List<JsonNode> getAllDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#getValueByKey(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<JsonNode> executeDocumentQuery(String designDocId,
			String viewName, String key) {

		return null;
	}

}
