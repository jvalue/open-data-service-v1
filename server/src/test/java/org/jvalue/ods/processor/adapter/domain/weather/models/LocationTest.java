/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models;

import org.junit.Assert;
import org.junit.Test;

public class LocationTest {

	private final Coordinate erlangen = new Coordinate(49.592410, 11.004174);

	@Test
	public void testConstructor() {
		Location location = new Location("Beverly Hills", "90210", erlangen, "us");

		Assert.assertEquals("Beverly Hills", location.getCity());
		Assert.assertEquals("90210", location.getZipCode());
		Assert.assertEquals(erlangen, location.getCoordinate());
		Assert.assertEquals("us", location.getCountryCode());
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
		Location location = new Location(null, null, erlangen);

		Assert.assertFalse(location.hasCity());
		Assert.assertFalse(location.hasZipCode());
		Assert.assertTrue(location.hasCoordinate());
	}


	@Test
	public void testUnknown() {
		Location location = new Location(Location.UNKNOWN, Location.UNKNOWN, null);

		Assert.assertFalse(location.hasCity());
		Assert.assertFalse(location.hasZipCode());
	}


	@Test
	public void testDefaultCountryCode() {
		Location location = new Location(null, null, erlangen);

		Assert.assertEquals("de", location.getCountryCode());
	}
}
