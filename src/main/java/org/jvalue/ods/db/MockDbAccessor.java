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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.schema.MapObjectType;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class MockDbAdapter.
 */
public class MockDbAccessor implements DbAccessor<JsonNode> {

	/** The list. */
	Map<String, Object> map = new HashMap<String, Object>();

	/** The is connected. */
	private boolean isConnected = false;

	/** The rn. */
	Random rn = new Random();

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

		if (id == null) {
			throw new IllegalArgumentException("id is null");
		}

		if (c == null) {
			throw new IllegalArgumentException("c is null");
		}

		if (id.length() == 0l) {
			throw new IllegalArgumentException("id is empty");
		}

		checkDbState();

		T ret = null;

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#insert(java.lang.Object)
	 */
	@Override
	public void insert(Object data) {
		if (data == null)
			throw new IllegalArgumentException("data is null");

		checkDbState();

		for (Entry<String, Object> s : map.entrySet()) {
			if (s.getValue().equals(data))
				throw new IllegalArgumentException();
		}

		map.put("" + rn.nextInt(1000000), data);
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAdapter#deleteDatabase()
	 */
	@Override
	public void deleteDatabase() {
		checkDbState();
		map.clear();
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

		checkDbState();

		List<JsonNode> ret = new LinkedList<JsonNode>();

		for (Entry<String, Object> e : map.entrySet()) {
			ret.add((JsonNode) e.getValue());
		}

		return ret;
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

		checkDbState();

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

		// key may be null

		return new LinkedList<JsonNode>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.db.DbAccessor#executeBulk(java.util.Collection)
	 */
	@Override
	public void executeBulk(Collection<MapObject> objects, MapObjectType schema) {

		checkDbState();

		if (objects == null) {
			throw new IllegalArgumentException("objects is null");
		}

	}

}
