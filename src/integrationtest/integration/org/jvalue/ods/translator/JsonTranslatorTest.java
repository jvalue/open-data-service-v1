/*
    Open Data Service
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
package integration.org.jvalue.ods.translator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.schema.AllowedValueTypes;
import org.jvalue.ods.data.schema.SimpleValueType;
import org.jvalue.ods.data.schema.ListComplexValueType;
import org.jvalue.ods.data.schema.MapComplexValueType;
import org.jvalue.ods.data.schema.GenericValueType;
import org.jvalue.ods.data.schema.ObjectTypeEnum;
import org.jvalue.ods.grabber.Translator;
import org.jvalue.ods.translator.JsonTranslator;

/**
 * The Class JsonTranslatorTest.
 */
public class JsonTranslatorTest {

	/** The Test url. */
	private final String TestUrl = "http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true";

	/** The translator. */
	private Translator translator;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		translator = new JsonTranslator();
		assertNotNull(translator);
	}

	/**
	 * Test Translate.
	 */
	@Test
	public void testTranslate() {
		GenericEntity gv = translator.translate(new DataSource(TestUrl, null));
		assertNotNull(gv);
	}

	/**
	 * Test Translate invalid source.
	 */
	@Test
	public void testTranslateInvalidSource() {
		GenericEntity gv = translator.translate(new DataSource("invalidsource",
				null));
		assertNull(gv);
	}

	/**
	 * Test Translate null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTranslateNullSource() {
		translator.translate(null);
	}

	/**
	 * Creates the open weather schema.
	 * 
	 * @return the schema
	 */
	private static GenericValueType createOpenWeatherSchema() {
		Map<String, GenericValueType> coord = new HashMap<String, GenericValueType>();
		coord.put("lon", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		coord.put("lat", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		MapComplexValueType coordSchema = new MapComplexValueType(coord);

		Map<String, GenericValueType> sys = new HashMap<String, GenericValueType>();
		sys.put("message", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		sys.put("country", AllowedValueTypes.getGenericValueType("java.lang.String"));
		sys.put("sunrise", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		sys.put("sunset", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		MapComplexValueType sysSchema = new MapComplexValueType(sys);

		Map<String, GenericValueType> weather = new HashMap<String, GenericValueType>();
		weather.put("id", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		weather.put("main", AllowedValueTypes.getGenericValueType("java.lang.String"));
		weather.put("description", AllowedValueTypes.getGenericValueType("java.lang.String"));
		weather.put("icon", AllowedValueTypes.getGenericValueType("java.lang.String"));
		MapComplexValueType weatherSchema = new MapComplexValueType(weather);
		List<GenericValueType> weatherList = new LinkedList<GenericValueType>();
		weatherList.add(weatherSchema);
		ListComplexValueType weatherListSchema = new ListComplexValueType(weatherList);

		Map<String, GenericValueType> main = new HashMap<String, GenericValueType>();
		main.put("temp", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		main.put("humidity", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		main.put("pressure", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		main.put("temp_min", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		main.put("temp_max", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		main.put("sea_level", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		main.put("grnd_level", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		MapComplexValueType mainSchema = new MapComplexValueType(main);

		Map<String, GenericValueType> wind = new HashMap<String, GenericValueType>();
		wind.put("speed", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		wind.put("gust", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		wind.put("deg", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		MapComplexValueType windSchema = new MapComplexValueType(wind);

		Map<String, GenericValueType> rain = new HashMap<String, GenericValueType>();
		rain.put("1h", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		rain.put("3h", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		MapComplexValueType rainSchema = new MapComplexValueType(rain);

		Map<String, GenericValueType> clouds = new HashMap<String, GenericValueType>();
		clouds.put("all", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		MapComplexValueType cloudsSchema = new MapComplexValueType(clouds);

		Map<String, GenericValueType> result = new HashMap<String, GenericValueType>();
		result.put("coord", coordSchema);
		result.put("sys", sysSchema);
		result.put("weather", weatherListSchema);
		result.put("base", AllowedValueTypes.getGenericValueType("java.lang.String"));
		result.put("main", mainSchema);
		result.put("wind", windSchema);
		result.put("rain", rainSchema);
		result.put("clouds", cloudsSchema);
		result.put("dt", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		result.put("id", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		result.put("name", AllowedValueTypes.getGenericValueType("java.lang.String"));
		result.put("cod", AllowedValueTypes.getGenericValueType("java.lang.Number"));
		MapComplexValueType resultSchema = new MapComplexValueType(result);

		return resultSchema;
	}

	/**
	 * Test Translate null source.
	 */
	@Test()
	public void testTranslateWithSchema() {
		AllowedValueTypes.addBaseObjectType("java.lang.String", new SimpleValueType("java.lang.String", ObjectTypeEnum.Domain));
		AllowedValueTypes.addBaseObjectType("java.lang.Number", new SimpleValueType("java.lang.Number", ObjectTypeEnum.Domain));
		AllowedValueTypes.addBaseObjectType("java.lang.Boolean", new SimpleValueType("java.lang.Boolean", ObjectTypeEnum.Domain));
		AllowedValueTypes.addBaseObjectType("Null", new SimpleValueType("Null", ObjectTypeEnum.Domain));
		
		
		GenericValueType genericObjectType = createOpenWeatherSchema();
		String url = "http://api.openweathermap.org/data/2.5/weather?q=Nuremberg,de";
		GenericEntity gv = translator.translate(new DataSource(url, genericObjectType));
		assertNotNull(gv);
	}

}
