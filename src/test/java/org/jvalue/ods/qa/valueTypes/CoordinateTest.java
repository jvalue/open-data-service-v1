package org.jvalue.ods.qa.valueTypes;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CoordinateTest {

	Double lat;
	Double lon;

	public CoordinateTest(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}

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

	@Test(expected = IllegalArgumentException.class)
	public final void testInvalid() {
		new Coordinate(lat, lon);
	}

	@Parameters
	public static Collection<Object[]> generate() {
		return Arrays.asList(new Object[][] { { 10000.0, 100000.0 },
				{ -91.0, 0.0 }, { 91.0, 0.0 }, { 0.0, -181.0 }, { 0.0, 181.0 },
				{ -567.0, 345.0 }

		});
	}

}