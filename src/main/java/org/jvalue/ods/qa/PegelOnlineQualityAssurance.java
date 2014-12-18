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
package org.jvalue.ods.qa;

import org.jvalue.EnumType;
import org.jvalue.ExactValueRestriction;
import org.jvalue.numbers.Range;
import org.jvalue.numbers.RangeBound;
import org.jvalue.ods.processor.filter.Filter;
import org.jvalue.ods.qa.valueTypes.Coordinate;
import org.jvalue.ods.utils.Log;
import org.jvalue.si.QuantityUnit;
import org.jvalue.si.QuantityUnitType;
import org.jvalue.si.SiUnit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The Class PegelOnlineQualityAssurance.
 */
abstract class PegelOnlineQualityAssurance implements Filter<Object, Object> {

	/**
	 * Instantiates a new pegel online quality assurance.
	 */
	public PegelOnlineQualityAssurance() {

	}

	/**
	 * Check value types.
	 */
	@SuppressWarnings("unchecked")
	protected Object doProcess(Object param) {

		List<Object> nodes = null;

		if (param instanceof List) {
			nodes = (List<Object>) param;
		} else if (param instanceof Map) {
			nodes = new LinkedList<Object>();
			nodes.add(param);
		} else {

			String errmsg = "PegelOnlineAssurance error, argument neither list nor map";
			System.err.println(errmsg);
			Log.error(errmsg);

			return param;
		}

		for (Object n : nodes) {

			try {

				HashMap<String, Object> doc = null;

				if (!(n instanceof Map)) {
					continue;
				}

				doc = (HashMap<String, Object>) n;

				HashMap<String, Object> coordinate = (HashMap<String, Object>) doc
						.get("coordinate");

				Object latitude = coordinate.get("latitude");
				Object longitude = coordinate.get("longitude");

				if (latitude != null && longitude != null) {

					try {

						new Coordinate((Double) latitude, (Double) longitude);
					} catch (IllegalArgumentException e) {
						String errmsg = "Invalid coordinate: " + latitude + ","
								+ longitude;
						System.err.println(errmsg);
						Log.error(errmsg);
					}
				} else {
					// no coordinates for this station
				}

				Double km = (Double) doc.get("km");

				QuantityUnitType q1 = createKmType();

				boolean kmIsValid = q1.isValidInstance(new QuantityUnit(
						km * 1000, SiUnit.m));

				if (!kmIsValid) {
					String errmsg = "Improbable km: " + km;
					System.err.println(errmsg);
					Log.error(errmsg);
				}

				List<Object> timeseries = (List<Object>) doc.get("timeseries");

				for (Object n2 : timeseries) {
					HashMap<String, Object> doc2 = (HashMap<String, Object>) n2;

					Integer equidistance = (Integer) doc2.get("equidistance");
					QuantityUnitType equiType = createEquidistanceType();
					boolean equiIsValid = equiType
							.isValidInstance(new QuantityUnit(
									equidistance * 60.0, SiUnit.s));

					if (!equiIsValid) {
						String errmsg = "Improbable equidistance: "
								+ equidistance;
						System.err.println(errmsg);
						Log.error(errmsg);
					}

					HashMap<String, Object> currentMeasurement = (HashMap<String, Object>) doc2
							.get("currentMeasurement");

					Object trend = currentMeasurement.get("trend");

					EnumType measurementTrendType = createMeasurementTrendType();

					boolean trendIsValid = measurementTrendType
							.isValidInstance(trend.toString());

					if (!trendIsValid) {
						String errmsg = "Invalid trend: " + trend;
						System.err.println(errmsg);
						Log.error(errmsg);
					}

					String shortname = (String) doc2.get("shortname");

					if (shortname.equals("W")) {

						Double value = (Double) currentMeasurement.get("value");

						QuantityUnitType qut = createWaterLevelType();

						boolean valueIsValid = qut
								.isValidInstance(new QuantityUnit(
										value / 100.0, SiUnit.m));

						if (!valueIsValid) {
							String errmsg = "Improbable water level: " + value;
							System.err.println(errmsg);
							Log.error(errmsg);
						}

						String stateMnwMhw = (String) currentMeasurement
								.get("stateMnwMhw");
						String stateNswHsw = (String) currentMeasurement
								.get("stateNswHsw");

						EnumType stateType = createStateType();

						boolean stateMnwMhwIsValid = stateType
								.isValidInstance(stateMnwMhw.toString());

						if (!stateMnwMhwIsValid) {
							String errmsg = "Impossible stateMnwMhw: "
									+ stateMnwMhw;
							System.err.println(errmsg);
							Log.error(errmsg);
						}

						boolean stateNswHswIsValid = stateType
								.isValidInstance(stateNswHsw.toString());

						if (!stateNswHswIsValid) {
							String errmsg = "Impossible stateNswHsw: "
									+ stateNswHsw;
							System.err.println(errmsg);
							Log.error(errmsg);
						}

					} else if (shortname.toString().equals("WT")) {
						Double value = (Double) currentMeasurement.get("value");

						QuantityUnitType qut = createTemperatureType();

						boolean valueIsValid = qut
								.isValidInstance(new QuantityUnit(
										value + 273.15, SiUnit.K));

						if (!valueIsValid) {
							String errmsg = "Improbable water temperature: "
									+ value;
							System.err.println(errmsg);
							Log.error(errmsg);
						}

					} else if (shortname.toString().equals("LF")) {
						Double value = (Double) currentMeasurement.get("value");

						QuantityUnitType qut = createElectricalConductivityType();

						boolean valueIsValid = qut
								.isValidInstance(new QuantityUnit(
										value / 10000, SiUnit.A.power(2)
												.multiply(SiUnit.s).power(3)
												.multiply(SiUnit.kg).power(-1)
												.multiply(SiUnit.m).power(-3)));

						if (!valueIsValid) {
							String errmsg = "Improbable electrical conductivity: "
									+ value;
							System.err.println(errmsg);
							Log.error(errmsg);
						}

					} else if (shortname.toString().equals("Q")) {
						Double value = (Double) currentMeasurement.get("value");

						QuantityUnitType qut = createRunoffType();

						boolean valueIsValid = qut
								.isValidInstance(new QuantityUnit(value,
										SiUnit.m.power(3).divide(SiUnit.s)));

						if (!valueIsValid) {
							String errmsg = "Improbable runoff: " + value;
							System.err.println(errmsg);
							Log.error(errmsg);
						}

					}
				}
			} catch (Exception e) {
				String errmsg = "PegelOnlineAssurance error";
				System.err.println(errmsg);
				e.printStackTrace();
				Log.error(errmsg);
				continue;
			}
		}

		return param;
	}


	protected void doOnComplete() {
		// nothing to do here
	}


	private static EnumType createMeasurementTrendType() {
		ExactValueRestriction<String> a = new ExactValueRestriction<String>(
				"-1");
		ExactValueRestriction<String> b = new ExactValueRestriction<String>("0");
		ExactValueRestriction<String> c = new ExactValueRestriction<String>("1");
		ExactValueRestriction<String> d = new ExactValueRestriction<String>(
				"-999");

		EnumType trendType = new EnumType(a.or(b).or(c).or(d));
		return trendType;
	}

	private static QuantityUnitType createWaterLevelType() {

		RangeBound<Double> low = new RangeBound<Double>(-50.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.m);
	}

	private static QuantityUnitType createTemperatureType() {

		RangeBound<Double> low = new RangeBound<Double>(200.0);
		RangeBound<Double> high = new RangeBound<Double>(350.0);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.K);
	}

	private static QuantityUnitType createElectricalConductivityType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.A.power(2).multiply(SiUnit.s)
				.power(3).multiply(SiUnit.kg).power(-1).multiply(SiUnit.m)
				.power(-3));
	}

	private static QuantityUnitType createKmType() {

		RangeBound<Double> low = new RangeBound<Double>(-6900000.0);
		RangeBound<Double> high = new RangeBound<Double>(6900000.0);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.m);
	}

	private static EnumType createStateType() {
		ExactValueRestriction<String> a = new ExactValueRestriction<String>(
				"low");
		ExactValueRestriction<String> b = new ExactValueRestriction<String>(
				"normal");
		ExactValueRestriction<String> c = new ExactValueRestriction<String>(
				"high");
		ExactValueRestriction<String> d = new ExactValueRestriction<String>(
				"unknown");
		ExactValueRestriction<String> e = new ExactValueRestriction<String>(
				"commented");
		ExactValueRestriction<String> f = new ExactValueRestriction<String>(
				"out-dated");

		EnumType trendType = new EnumType(a.or(b).or(c).or(d).or(e).or(f));
		return trendType;
	}

	private static QuantityUnitType createEquidistanceType() {

		RangeBound<Double> low = new RangeBound<Double>(0.0);
		RangeBound<Double> high = new RangeBound<Double>(Double.MAX_VALUE);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.s);
	}

	private static QuantityUnitType createRunoffType() {

		RangeBound<Double> low = new RangeBound<Double>(-100000.0);
		RangeBound<Double> high = new RangeBound<Double>(100000.0);
		Range<Double> range = new Range<Double>(low, high);
		return new QuantityUnitType(range, SiUnit.m.power(3).divide(SiUnit.s));
	}

}
