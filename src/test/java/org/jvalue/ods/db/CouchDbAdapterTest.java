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

import org.ektorp.DbAccessException;
import org.ektorp.support.CouchDbDocument;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class CouchDbInserterTest.
 */
public class CouchDbAdapterTest {

	/** The test db name. */
	private final String testDbName = "CouchDbInserterTest";

	/** The data. */
	private final CouchDbDocument data = new CouchDbDocument();

	/** The couch db inserter. */
	private DbAdapter<CouchDbDocument> couchDbAdapter;

	/**
	 * Initialize.
	 */
	@Before
	public void initialize() {
		// This test needs installed Apache CouchDB!
		couchDbAdapter = new CouchDbAdapter(testDbName);
		assertNotNull(couchDbAdapter);
	}

	/**
	 * Clean up.
	 */
	@After
	public void cleanUp() {
		couchDbAdapter.deleteDatabase();
	}

	// Constructor Tests
	/**
	 * Test couch db inserter constructor empty database name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouchDbInserterConstructorEmptyDatabaseName() {
		String databaseName = "";
		DbAdapter<CouchDbDocument> couchDbAdapter = new CouchDbAdapter(
				databaseName);
		assertNotNull(couchDbAdapter);
	}

	/**
	 * Test couch db inserter constructor null database name.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCouchDbInserterConstructorNullDatabaseName() {
		String databaseName = null;
		DbAdapter<CouchDbDocument> couchDbAdapter = new CouchDbAdapter(
				databaseName);
		assertNotNull(couchDbAdapter);
	}

	// Method Tests
	/**
	 * Test insert and getFirstDocumentId method.
	 */
	@Test
	public void testInsert() {
		String id = couchDbAdapter.insert(data);
		assertEquals(data.getId(), id);
	}

	/**
	 * Test insert twice same data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTwiceSameData() {
		String id = couchDbAdapter.insert(data);
		assertEquals(data.getId(), id);
		id = couchDbAdapter.insert(data);
	}

	/**
	 * Test insert null data.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInsertNullData() {
		couchDbAdapter.insert(null);
		couchDbAdapter.deleteDatabase();
	}

	/**
	 * Test update invalid revision.
	 */
	@Test(expected = DbAccessException.class)
	public void testUpdateInvalidRevision() {
		String id = "42";
		String revision = "invalidRevision";

		CouchDbDocument doc = new CouchDbDocument();
		doc.setId(id);
		doc.setRevision(revision);

		couchDbAdapter.update(doc);
	}

	/**
	 * Test update empty id.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUpdateEmptyId() {
		String id = "";

		CouchDbDocument doc = new CouchDbDocument();
		doc.setId(id);

		couchDbAdapter.update(doc);
	}

	/**
	 * Test get last document id.
	 */
	@Test
	public void testGetLastDocumentId() {
		String id = couchDbAdapter.insert(data);
		assertEquals(data.getId(), id);

		String lastId = couchDbAdapter.getLastDocumentId();
		assertEquals(lastId, id);
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

}
