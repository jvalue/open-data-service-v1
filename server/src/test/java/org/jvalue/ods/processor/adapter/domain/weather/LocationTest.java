package org.jvalue.ods.processor.adapter.domain.weather;

import org.junit.Assert;
import org.junit.Test;

public class LocationTest {

	@Test
	public void testConstructor() {
		Location location = new Location("Beverly Hills", "90210", "10:12");

		Assert.assertEquals("Beverly Hills", location.getCity());
		Assert.assertEquals("90210", location.getZipCode());
		Assert.assertEquals("10:12", location.getCoordinate());
	}

	@Test
	public void testHasCity() {
		Location location = new Location("Beverly Hills", null, null);

		Assert.assertTrue(location.hasCity());
		Assert.assertFalse(location.hasZipCode());
		Assert.assertFalse(location.hasCoordinate());
	}

	@Test
	public void testHasZipCode() {
		Location location = new Location(null , "90210", null);

		Assert.assertFalse(location.hasCity());
		Assert.assertTrue(location.hasZipCode());
		Assert.assertFalse(location.hasCoordinate());
	}

	@Test
	public void testHasCoordinate() {
		Location location = new Location(null, null, "10:12");

		Assert.assertFalse(location.hasCity());
		Assert.assertFalse(location.hasZipCode());
		Assert.assertTrue(location.hasCoordinate());
	}
}
