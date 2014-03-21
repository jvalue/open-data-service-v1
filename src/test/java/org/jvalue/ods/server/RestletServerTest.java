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
package org.jvalue.ods.server;

import java.net.BindException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * The Class RestletServerTest.
 */
@RunWith(value = Parameterized.class)
public class RestletServerTest {

	/** The port. */
	private int port;

	/**
	 * Instantiates a new restlet adapter test.
	 * 
	 * @param port
	 *            the port
	 */
	public RestletServerTest(int port) {
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
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = IllegalArgumentException.class)
	public void RestletServerWithIllegalPorts() throws Exception {
		new RestletServer(port);
	}

	/**
	 * Test initialize.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testInitialize() throws Exception {
		RestletServer ra = new RestletServer(8800);
		ra.initialize();
		ra.disconnect();

	}

	/**
	 * Test double initialize.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test(expected = BindException.class)
	public void testDoubleInitialize() throws Exception {
		new RestletServer(8900).initialize();
		new RestletServer(8900).initialize();
	}

	/**
	 * Test stop without init.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testStopWithoutInit() throws Exception {
		RestletServer ra = new RestletServer(8900);
		ra.disconnect();

	}

}
