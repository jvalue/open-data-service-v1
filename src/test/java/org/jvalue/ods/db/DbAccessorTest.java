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

import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.generic.MapValue;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class DbAccessorTest.
 */
public class DbAccessorTest {

	/** The test db name. */
	private final String testDbName = "DbAccessorTest";

	/** The data. */
	private final Object data = new Object();

	/** The couch db inserter. */
	private DbAccessor<JsonNode> couchDbAdapter;

	/**
	 * Initialize.
	 */
	@Before
	public void initialize() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.connect();
	}

	/**
	 * Clean up.
	 */
	@After
	public void cleanUp() {
		couchDbAdapter.connect();
		if (couchDbAdapter.isConnected()) {

			couchDbAdapter.deleteDatabase();
		}
	}

	// Constructor Tests
	/**
	 * Test couch db inserter constructor empty database name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouchDbInserterConstructorEmptyDatabaseName() {
		String databaseName = "";
		DbAccessor<JsonNode> couchDbAdapter = DbFactory
				.createMockDbAccessor(databaseName);
		assertNotNull(couchDbAdapter);
	}

	/**
	 * Test couch db inserter constructor null database name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouchDbInserterConstructorNullDatabaseName() {
		String databaseName = null;
		DbAccessor<JsonNode> couchDbAdapter = DbFactory
				.createMockDbAccessor(databaseName);
		assertNotNull(couchDbAdapter);
	}

	/**
	 * Test connect.
	 */
	@Test
	public void testConnect() {
		DbAccessor<JsonNode> couchDbAdapter = DbFactory
				.createMockDbAccessor(testDbName);
		couchDbAdapter.connect();
	}

	// Method Tests

	/**
	 * Test insert twice same data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTwiceSameData() {
		couchDbAdapter.insert(data);
		couchDbAdapter.insert(data);
	}

	/**
	 * Test insert without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testInsertWithoutConnect() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.insert(data);
	}

	/**
	 * Test insert null data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullData() {
		couchDbAdapter.insert(null);
	}

	/**
	 * Test update null data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullData() {
		couchDbAdapter.update(null);
	}

	/**
	 * Test update without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithoutConnect() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.update(data);
	}

	/**
	 * Test get document null classname.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDocumentNullClassname() {
		couchDbAdapter.getDocument(null, "");
	}

	/**
	 * Test get document null id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDocumentNullId() {
		couchDbAdapter.getDocument(Object.class, null);
	}

	/**
	 * Test get document empty id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDocumentEmptyId() {
		couchDbAdapter.getDocument(Object.class, "");
	}

	/**
	 * Test get document without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetDocumentWithoutConnect() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.getDocument(Object.class, "1");
	}

	/**
	 * Test delete database without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDeleteDatabaseWithoutConnect() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.deleteDatabase();
	}

	/**
	 * Test get all documents without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetAllDocumentsWithoutConnect() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.getAllDocuments();
	}

	/**
	 * Test get all documents new db.
	 */
	@Test
	public void testGetAllDocumentsNewDb() {
		couchDbAdapter = DbFactory.createMockDbAccessor("foo");
		assertNotNull(couchDbAdapter);
		couchDbAdapter.connect();
		couchDbAdapter.getAllDocuments();
		couchDbAdapter.deleteDatabase();
	}

	/**
	 * Test execute document query without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testExecuteDocumentQueryWithoutConnect() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.executeDocumentQuery("xxx", "xxx", null);
	}

	/**
	 * Test execute document query null doc id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryNullDocId() {
		couchDbAdapter.executeDocumentQuery(null, "xxx", null);
	}

	/**
	 * Test execute document query null view name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryNullViewName() {
		couchDbAdapter.executeDocumentQuery("xxx", null, null);
	}

	/**
	 * Test execute document query empty doc id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryEmptyDocId() {
		couchDbAdapter.executeDocumentQuery("", "xxx", null);
	}

	/**
	 * Test execute document query empty view name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryEmptyViewName() {
		couchDbAdapter.executeDocumentQuery("xxx", "", null);
	}

	/**
	 * Test execute document query new db.
	 */
	@Test
	public void testExecuteDocumentQueryNewDb() {
		couchDbAdapter = DbFactory.createMockDbAccessor("foo");
		assertNotNull(couchDbAdapter);
		couchDbAdapter.connect();
		couchDbAdapter.executeDocumentQuery("xxx", "xxx", null);
		couchDbAdapter.deleteDatabase();
	}

	/**
	 * Test execute bulk without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testExecuteBulkWithoutConnect() {
		couchDbAdapter = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.executeBulk(new LinkedList<MapValue>(), null);
	}

	/**
	 * Test execute bulk new db.
	 */
	@Test
	public void testExecuteBulkNewDb() {
		couchDbAdapter = DbFactory.createMockDbAccessor("foo");
		assertNotNull(couchDbAdapter);
		couchDbAdapter.connect();
		couchDbAdapter.executeBulk(new LinkedList<MapValue>(), null);
		couchDbAdapter.deleteDatabase();
	}

}
