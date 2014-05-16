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
package org.jvalue.ods.qa.valueTypes;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * The Class CoordinateTest.
 */
@RunWith(Parameterized.class)
public class CoordinateTest {

	/** The lat. */
	Double lat;

	/** The lon. */
	Double lon;

	/**
	 * Instantiates a new coordinate test.
	 * 
	 * @param lat
	 *            the lat
	 * @param lon
	 *            the lon
	 */
	public CoordinateTest(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Test valid.
	 */
	@Test
	public final void testValid() {
		new Coordinate(0.0, 0.0);
		new Coordinate(-90.0, 0.0);
		new Coordinate(90.0, 0.0);
		new Coordinate(0.0, -180.0);
		new Coordinate(0.0, 180.0);
		new Coordinate(-55.0, 55.0);
		new Coordinate(90.0, 180.0);
		new Coordinate(-90.0, -180.0);

	}

	/**
	 * Test invalid.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testInvalid() {
		new Coordinate(lat, lon);
	}

	/**
	 * Generate.
	 * 
	 * @return the collection
	 */
	@Parameters
	public static Collection<Object[]> generate() {
		return Arrays.asList(new Object[][] { { 10000.0, 100000.0 },
				{ -91.0, 0.0 }, { 91.0, 0.0 }, { 0.0, -181.0 }, { 0.0, 181.0 },
				{ -567.0, 345.0 }

		});
	}

}