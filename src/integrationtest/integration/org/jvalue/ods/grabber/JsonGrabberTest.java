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
package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.grabber.JsonGrabber;

/**
 * The Class JsonGrabberTest.
 */
public class JsonGrabberTest {

	/** The Test url. */
	private final String TestUrl = "http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true";
	
/** The grabber. */
private JsonGrabber grabber;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		grabber = new JsonGrabber();
		assertNotNull(grabber);
	}

	/**
	 * Test grab.
	 */
	@Test
	public void testGrab() {
		GenericValue gv = grabber.grab(TestUrl);
		assertNotNull(gv);			
	}
	
	/**
	 * Test grab invalid source.
	 */
	@Test
	public void testGrabInvalidSource() {
		GenericValue gv = grabber.grab("invalidsource");
		assertNull(gv);			
	}
	
	/**
	 * Test grab null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		grabber.grab(null);				
	}
}
