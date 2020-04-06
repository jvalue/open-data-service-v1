/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain.weather.models.extended;

import org.junit.Assert;
import org.junit.Test;

public class RadiationTypeTest {

	private static final double DELTA = 0.001;

	private static final double wattPerSecondPerM2 = 708000.0;
	private static final double wattPerHourPerM2 = 196.6666;

	@Test
	public void testToWattPerSquareMeter_JoulePerCm2() {
		double joulePerCm2 = 70.8;

		double result = RadiationType.JOULE_PER_SQUARE_CENTIMETER.toWattPerSquareMeter(joulePerCm2, 1);
		Assert.assertEquals(wattPerSecondPerM2, result, DELTA);

		result = RadiationType.JOULE_PER_SQUARE_CENTIMETER.toWattPerSquareMeter(joulePerCm2, 3600);
		Assert.assertEquals(wattPerHourPerM2, result, DELTA);
	}

	@Test
	public void testToWattPerSquareMeter_KJoulePerM2() {
		double kJoulePerM2 =708;

		double result = RadiationType.KILOJOULE_PER_SQUARE_METER.toWattPerSquareMeter(kJoulePerM2, 1);
		Assert.assertEquals(wattPerSecondPerM2, result, DELTA);

		result = RadiationType.KILOJOULE_PER_SQUARE_METER.toWattPerSquareMeter(kJoulePerM2, 3600);
		Assert.assertEquals(wattPerHourPerM2, result, DELTA);
	}

	@Test
	public void testToWattPerSquareMeter_JoulePerM2() {
		double joulePerM2 = 708000.0;

		double result = RadiationType.JOULE_PER_SQUARE_METER.toWattPerSquareMeter(joulePerM2, 1);
		Assert.assertEquals(wattPerSecondPerM2, result, DELTA);

		result = RadiationType.JOULE_PER_SQUARE_METER.toWattPerSquareMeter(joulePerM2, 3600);
		Assert.assertEquals(wattPerHourPerM2, result, DELTA);
	}
}
