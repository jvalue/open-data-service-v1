package org.jvalue.ods.qa.valueTypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.jvalue.ExactValueRestriction;
import org.jvalue.StringType;
import org.jvalue.ValueType;
import org.jvalue.numbers.NumberType;
import org.jvalue.numbers.Range;

public class MapTypeTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void test() {

		ValueType<Double> latitudeType = new NumberType<Double>(
				new Range<Double>(-90.0, 90.0));
		ValueType<Double> longitudeType = new NumberType<Double>(
				new Range<Double>(-180.0, 180.0));

		ValueType<String> strType = new StringType(
				new ExactValueRestriction<String>("bla"));

		Map<String, ValueType<?>> map = new HashMap<>();

		map.put("latitude", latitudeType);
		map.put("longitude", longitudeType);
		map.put("name", strType);

		Map<String, Object> map2 = new HashMap<>();

		map2.put("latitude", 0.0);
		map2.put("longitude", 0.0);
		map2.put("name", "bla");

		assertTrue(new MapType(map).isValidInstance(map2));

		map2.put("age", "bla");

		assertTrue(new MapType(map).isValidInstance(map2));

		map2.put("name", "blaf");

		assertFalse(new MapType(map).isValidInstance(map2));
	}

}
