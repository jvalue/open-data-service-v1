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
package integration.org.jvalue.ods.db;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class DbAdapterTest.
 */
public class CouchDbAccessorTest {

	/** The test db name. */
	private final String testDbName = "DbAccessorTest";

	/** The data. */
	private final CouchDbDocument data = new CouchDbDocument();

	/** The couch db inserter. */
	private DbAccessor<JsonNode> couchDbAdapter;

	/**
	 * Initialize.
	 */
	@Before
	public void initialize() {
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
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
				.createDbAccessor(databaseName);
		assertNotNull(couchDbAdapter);
	}

	/**
	 * Test couch db inserter constructor null database name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouchDbInserterConstructorNullDatabaseName() {
		String databaseName = null;
		DbAccessor<JsonNode> couchDbAdapter = DbFactory
				.createDbAccessor(databaseName);
		assertNotNull(couchDbAdapter);
	}

	/**
	 * Test connect.
	 */
	@Test
	public void testConnect() {
		DbAccessor<JsonNode> couchDbAdapter = DbFactory
				.createDbAccessor(testDbName);
		couchDbAdapter.connect();
	}

	// Method Tests

	/**
	 * Test insert twice same data.
	 */
	@Test(expected = DbException.class)
	public void testInsertTwiceSameData() {
		couchDbAdapter.insert(data);
		couchDbAdapter.insert(data);
	}

	/**
	 * Test insert without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testInserWithoutConnect() {
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
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
	 * Test update.
	 */
	@Test
	public void testUpdate() {
		String id = "42";

		CouchDbDocument doc = new CouchDbDocument();
		doc.setId(id);

		String rev = doc.getRevision();
		couchDbAdapter.update(doc);

		// Revision of document should have changed
		assertNotEquals(doc.getRevision(), rev);
	}

	/**
	 * Test update null data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNullData() {
		couchDbAdapter.update(null);
	}

	/**
	 * Test update invalid revision.
	 */
	@Test(expected = DbException.class)
	public void testUpdateInvalidRevision() {
		String id = "42";
		String revision = "invalidRevision";

		CouchDbDocument doc = new CouchDbDocument();
		doc.setId(id);
		doc.setRevision(revision);

		couchDbAdapter.update(doc);
	}

	/**
	 * Test update without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testUpdateWithoutConnect() {
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
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
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.getDocument(Object.class, "1");
	}

	/**
	 * Test delete database without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testDeleteDatabaseWithoutConnect() {
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.deleteDatabase();
	}

	/**
	 * Test get all documents without connect.
	 */
	@Test(expected = IllegalStateException.class)
	public void testGetAllDocumentsWithoutConnect() {
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.getAllDocuments();
	}

	/**
	 * Test get all documents new db.
	 */
	@Test
	public void testGetAllDocumentsNewDb() {
		couchDbAdapter = DbFactory.createDbAccessor("foo");
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
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
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
	@Test(expected = DocumentNotFoundException.class)
	public void testExecuteDocumentQueryWithoutDesignDocument() {
		couchDbAdapter = DbFactory.createDbAccessor("foo");
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
		couchDbAdapter = DbFactory.createDbAccessor(testDbName);
		assertNotNull(couchDbAdapter);
		couchDbAdapter.executeBulk(new LinkedList<MapObject>(), null);
	}

	/**
	 * Test execute bulk new db.
	 */
	@Test
	public void testExecuteBulkNewDb() {
		couchDbAdapter = DbFactory.createDbAccessor("foo");
		assertNotNull(couchDbAdapter);
		couchDbAdapter.connect();

		List<MapObject> list = new LinkedList<>();
		list.add(new MapObject());
		list.add(new MapObject());
		list.add(new MapObject());

		couchDbAdapter.executeBulk(list, null);
		couchDbAdapter.deleteDatabase();
	}

	/**
	 * Test execute bulk.
	 */
	@Test
	public void testExecuteBulk() {

		List<MapObject> list = new LinkedList<>();
		list.add(new MapObject());
		list.add(new MapObject());
		list.add(new MapObject());

		couchDbAdapter.executeBulk(list, null);
	}

}
