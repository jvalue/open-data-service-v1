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

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.server.pegelonline.PegelOnlineRouter;
import org.restlet.Restlet;

/**
 * The Class PegelOnlineRouterTest.
 */
public class PegelOnlineRouterTest {

	/** The router. */
	PegelOnlineRouter router;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		router = new PegelOnlineRouter(
				DbFactory.createMockDbAccessor("DbAccessorTest"));
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
