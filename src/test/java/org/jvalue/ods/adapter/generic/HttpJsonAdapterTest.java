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
package org.jvalue.ods.adapter.generic;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class HttpJsonAdapterTest.
 */
public class HttpJsonAdapterTest {

	/** The adapter. */
	HttpJsonAdapter adapter;	
	
	/** The Test url. */
	final String TestUrl = "http://www.pegelonline.wsv.de/webservices/rest-api/v2/waters.json?includeStations=true&includeTimeseries=true&includeCurrentMeasurement=true";
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		adapter = new HttpJsonAdapter(TestUrl);
	}

	/**
	 * Test http json adapter with null url.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHttpJsonAdapterWithNullUrl() throws IOException {
		adapter = new HttpJsonAdapter(null);
	}

	/**
	 * Test http json adapter with empty url.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHttpJsonAdapterWithEmptyUrl() throws IOException {
		adapter = new HttpJsonAdapter("");
	}
	
	/**
	 * Test http json adapter with invalid url.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(expected = IOException.class)
	public void testHttpJsonAdapterWithInvalidUrl() throws IOException {
		adapter = new HttpJsonAdapter("invalidUrl");
		assertNotNull(adapter);
		adapter.getJSON("");
	}
	

	/**
	 * Test get json.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test()
	public void testGetJSON() throws IOException {
		adapter.getJSON("UTF-8");
	}
	
	/**
	 * Test get json with empty charset.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetJSONWithEmptyCharset() throws IOException {
		String json = adapter.getJSON("");
		assertNotNull(json);
	}
	
	/**
	 * Test get json with null charset.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetJSONWithNullCharset() throws IOException {
		adapter.getJSON(null);
	}

}
