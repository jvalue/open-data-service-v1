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
package integration.org.jvalue.ods.grabber.openstreetmap.overpass;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.openstreetmap.overpass.Overpass;
import org.jvalue.ods.grabber.openstreetmap.overpass.OverpassGrabber;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * The Class OverpassGrabberTest.
 */
public class OverpassGrabberTest {

	/** The grabber. */
	OverpassGrabber grabber;

	/**
	 * Initialize.
	 */
	@Before
	public void initialize() {
		grabber = new OverpassGrabber();
		assertNotNull(grabber);
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
		Overpass result = grabber.getLocationData("Rothsee");
		assertNotNull(result);
	}

}
