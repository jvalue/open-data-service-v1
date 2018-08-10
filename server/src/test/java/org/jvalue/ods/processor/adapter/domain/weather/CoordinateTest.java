package org.jvalue.ods.processor.adapter.domain.weather;

import org.junit.Assert;
import org.junit.Test;

public class CoordinateTest {
	private final static double DELTA = 0.0000001;
	private final  Coordinate erlangen = new Coordinate(49.592410, 11.004174);

	@Test
	public void testConstructor() {
		Assert.assertEquals(49.592410, erlangen.getLatitude(), DELTA);
		Assert.assertEquals(11.004174, erlangen.getLongitude(), DELTA);
	}


	@Test
	public void testEquals() {
		Coordinate other = new Coordinate(49.592410, 11.004174);

		Assert.assertEquals(erlangen, other);
	}


	@Test
	public void testToString() {
		Assert.assertEquals("49.59241:11.004174", erlangen.toString());
	}
}
