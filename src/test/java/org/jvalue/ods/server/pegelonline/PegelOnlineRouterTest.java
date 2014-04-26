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
package org.jvalue.ods.server.pegelonline;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.main.RouterFactory;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class PegelOnlineRouterTest.
 */
public class PegelOnlineRouterTest {

	/** The router. */
	PegelOnlineRouter router;

	DbAccessor<JsonNode> mockAccessor;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		router = RouterFactory.createPegelOnlineRouter();
		mockAccessor = DbFactory.createMockDbAccessor("DbAccessorTest");
		router.setDbAccessor(mockAccessor);
	}

	/**
	 * Clean up.
	 */
	@After
	public void cleanUp() {
		mockAccessor.connect();
		if (mockAccessor.isConnected()) {

			mockAccessor.deleteDatabase();
		}
	}

	/**
	 * Test get routes.
	 */
	@Test
	public void testGetRoutes() {
		Map<String, Restlet> routes = router.getRoutes();
		assertNotNull(routes);

		// Get Map in Set interface to get key and value
		Set<Entry<String, Restlet>> s = routes.entrySet();

		// Move next key and value of Map by iterator
		Iterator<Entry<String, Restlet>> it = s.iterator();

		while (it.hasNext()) {
			Entry<String, Restlet> m = it.next();
			assertNotNull(m.getKey());
			assertNotNull(m.getValue());
		}
	}

}
