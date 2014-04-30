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
package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.translator.XmlTranslator;

/**
 * The Class XmlGrabberTest.
 */
public class XmlGrabberTest {

	/** The grabber. */
	private XmlTranslator grabber;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		grabber = new XmlTranslator();
		assertNotNull(grabber);
	}

	/**
	 * Test grab.
	 */
	@Test
	public void testGrab() {
		GenericValue gv = grabber.translate(new DataSource("/nbgcity.osm", null));
		assertNotNull(gv);
	}

	/**
	 * Test grab not existing file.
	 */
	@Test
	public void testGrabNotExistingFile() {
		GenericValue gv = grabber.translate(new DataSource("NotExistingFile", null));
		assertNull(gv);
	}

	/**
	 * Test grab null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		grabber.translate(null);
	}
}
