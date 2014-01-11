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
package org.jvalue.ods.restlet;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.jvalue.ods.adapter.pegelonline.PegelOnlineRouter;
import org.restlet.Restlet;

/**
 * The Class RestletAdapterTest.
 */
@RunWith(value = Parameterized.class)
public class RestletAdapterTest {

	/**
	 * Restlet adapter with null routes.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void RestletAdapterWithNullRoutes() throws Exception {
		new RestletAdapter(null, 8182);
	}

	/**
	 * Restlet adapter with empty routes.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void RestletAdapterWithEmptyRoutes() throws Exception {
		new RestletAdapter(new HashMap<String, Restlet>(), 8182);
	}

	/** The port. */
	private int port;

	/**
	 * Instantiates a new restlet adapter test.
	 *
	 * @param port the port
	 */
	public RestletAdapterTest(int port) {
		this.port = port;
	}

	/**
	 * Data.
	 *
	 * @return the collection
	 */
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { -50 }, { 0 }, { 1000 }, { 1023 },
				{ 49152 }, { 50000 } };
		return Arrays.asList(data);
	}

	/**
	 * Restlet adapter with illegal ports.
	 *
	 * @throws Exception the exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void RestletAdapterWithIllegalPorts() throws Exception {

		PegelOnlineRouter poRouter = new PegelOnlineRouter();

		HashMap<String, Restlet> combinedRouter = new HashMap<String, Restlet>();
		combinedRouter.putAll(poRouter.getRoutes());

		new RestletAdapter(combinedRouter, port);
		new RestletAdapter(combinedRouter, port);
		new RestletAdapter(combinedRouter, port);
	}

}
