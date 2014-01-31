/*
    Open Data Service
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
package integration.org.jvalue.ods.grabber.openstreetmap.nominatim;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.opensteetmap.nominatim.NominatimQueryResult;
import org.jvalue.ods.grabber.openstreetmap.nominatim.NominatimGrabber;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * The Class NominatimGrabberTest.
 */
public class NominatimGrabberTest {

	/** The adapter. */
	NominatimGrabber adapter;

	/**
	 * Initialize.
	 */
	@Before
	public void initialize() {
		adapter = new NominatimGrabber();
		assertNotNull(adapter);
	}

	/**
	 * Test get nominatim data.
	 * 
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetNominatimData() throws JsonParseException,
			JsonMappingException, IOException {
		List<NominatimQueryResult> result = adapter.getNominatimData("Rothsee");
		assertNotNull(result);
		for (NominatimQueryResult r : result) {
			assertNotNull(r);
		}
	}

}
