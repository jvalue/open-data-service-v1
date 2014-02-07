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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.ektorp.support.CouchDbDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
		if (couchDbAdapter.isConnected())
			couchDbAdapter.deleteDatabase();
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
	@Test(expected = IllegalArgumentException.class)
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
	 * Test get last document id.
	 */
	@Test
	public void testGetLastDocumentId() {
		couchDbAdapter.insert(data);
		String id = data.getId();

		String lastId = couchDbAdapter.getLastDocumentId();
		assertEquals(lastId, id);
	}

	/**
	 * Test get last document id empty database.
	 */
	@Test(expected = DbException.class)
	public void testGetLastDocumentIdEmptyDatabase() {
		couchDbAdapter.deleteDatabase();
		couchDbAdapter.getLastDocumentId();
	}

}
