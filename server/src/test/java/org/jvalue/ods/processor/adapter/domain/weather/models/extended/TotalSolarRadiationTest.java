/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import org.junit.Assert;
import org.junit.Test;

public class TotalSolarRadiationTest {

	private static final double DELTA = 0.001;

	@Test
	public void testEquals() {
		TotalSolarRadiation solar1 = new TotalSolarRadiation(70.1, RadiationType.JOULE_PER_SQUARE_CENTIMETER);
		TotalSolarRadiation solar2 = new TotalSolarRadiation(70.1, RadiationType.JOULE_PER_SQUARE_CENTIMETER);
		TotalSolarRadiation solar3 = new TotalSolarRadiation(70.101, RadiationType.JOULE_PER_SQUARE_CENTIMETER);

		Assert.assertEquals(solar1, solar2);
		Assert.assertNotEquals(solar1, solar3);
	}

	@Test
	public void testToWattPerHourPerSquareMeter() {
		double joulePerCm2 = 70.8;
		TotalSolarRadiation solar = new TotalSolarRadiation(joulePerCm2, RadiationType.JOULE_PER_SQUARE_CENTIMETER);

		Assert.assertEquals(196.66, solar.toWattPerHourPerSquareMeter(), DELTA);
	}


}
