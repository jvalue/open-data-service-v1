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
	private DbAccessor<JsonNode> couchDbAccessor;

	/**
	 * Initialize.
	 */
	@Before
	public void initialize() {
		couchDbAccessor = DbFactory.createMockDbAccessor(testDbName);
		assertNotNull(couchDbAccessor);
		couchDbAccessor.connect();
	}

	/**
	 * Clean up.
	 */
	@After
	public void cleanUp() {
		couchDbAccessor.connect();
		if (couchDbAccessor.isConnected()) {

			couchDbAccessor.deleteDatabase();
		}
	}

	// Constructor Tests
	/**
	 * Test couch db inserter constructor empty database name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouchDbInserterConstructorEmptyDatabaseName() {
		String databaseName = "";
		DbFactory.createMockDbAccessor(databaseName);
	}

	/**
	 * Test couch db inserter constructor null database name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouchDbInserterConstructorNullDatabaseName() {
		String databaseName = null;
		DbFactory.createMockDbAccessor(databaseName);
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
		couchDbAccessor.insert(data);
		couchDbAccessor.insert(data);
	}

	/**
	 * Test insert without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testInsertWithoutConnect() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		couchDbFooAccessor.insert(data);
	}

	/**
	 * Test insert null data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullData() {
		couchDbAccessor.insert(null);
	}

	/**
	 * Test update null data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullData() {
		couchDbAccessor.update(null);
	}

	/**
	 * Test update without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithoutConnect() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		couchDbFooAccessor.update(data);
	}

	/**
	 * Test get document null classname.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDocumentNullClassname() {
		couchDbAccessor.getDocument(null, "");
	}

	/**
	 * Test get document null id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDocumentNullId() {
		couchDbAccessor.getDocument(Object.class, null);
	}

	/**
	 * Test get document empty id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetDocumentEmptyId() {
		couchDbAccessor.getDocument(Object.class, "");
	}

	/**
	 * Test get document without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetDocumentWithoutConnect() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		couchDbFooAccessor.getDocument(Object.class, "1");
	}

	/**
	 * Test delete database without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDeleteDatabaseWithoutConnect() {
		/** The couch db inserter. */
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		couchDbFooAccessor.deleteDatabase();
	}

	/**
	 * Test get all documents without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetAllDocumentsWithoutConnect() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		couchDbFooAccessor.getAllDocuments();
	}

	/**
	 * Test get all documents new db.
	 */
	@Test
	public void testGetAllDocumentsNewDb() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		assertNotNull(couchDbFooAccessor);
		couchDbFooAccessor.connect();
		couchDbFooAccessor.getAllDocuments();
		couchDbFooAccessor.deleteDatabase();
	}

	/**
	 * Test execute document query without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testExecuteDocumentQueryWithoutConnect() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		couchDbFooAccessor.executeDocumentQuery("xxx", "xxx", null);
	}

	/**
	 * Test execute document query null doc id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryNullDocId() {
		couchDbAccessor.executeDocumentQuery(null, "xxx", null);
	}

	/**
	 * Test execute document query null view name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryNullViewName() {
		couchDbAccessor.executeDocumentQuery("xxx", null, null);
	}

	/**
	 * Test execute document query empty doc id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryEmptyDocId() {
		couchDbAccessor.executeDocumentQuery("", "xxx", null);
	}

	/**
	 * Test execute document query empty view name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteDocumentQueryEmptyViewName() {
		couchDbAccessor.executeDocumentQuery("xxx", "", null);
	}

	/**
	 * Test execute document query new db.
	 */
	@Test
	public void testExecuteDocumentQueryNewDb() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		assertNotNull(couchDbFooAccessor);
		couchDbFooAccessor.connect();
		couchDbFooAccessor.executeDocumentQuery("xxx", "xxx", null);
		couchDbFooAccessor.deleteDatabase();
	}

	/**
	 * Test execute bulk without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testExecuteBulkWithoutConnect() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		couchDbFooAccessor.executeBulk(new LinkedList<MapValue>(), null);
	}

	/**
	 * Test execute bulk new db.
	 */
	@Test
	public void testExecuteBulkNewDb() {
		DbAccessor<JsonNode> couchDbFooAccessor = DbFactory
				.createMockDbAccessor("foo");
		assertNotNull(couchDbFooAccessor);
		couchDbFooAccessor.connect();
		couchDbFooAccessor.executeBulk(new LinkedList<MapValue>(), null);
		couchDbFooAccessor.deleteDatabase();
	}

}
