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
import org.jvalue.ods.data.schema.AllowedBaseObjectTypes;
import org.jvalue.ods.data.schema.ListObjectType;
import org.jvalue.ods.data.schema.MapObjectType;
import org.jvalue.ods.data.schema.GenericObjectType;
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
	private static GenericObjectType createOpenWeatherSchema() {
		Map<String, GenericObjectType> coord = new HashMap<String, GenericObjectType>();
		coord.put("lon", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		coord.put("lat", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType coordSchema = new MapObjectType(coord);

		Map<String, GenericObjectType> sys = new HashMap<String, GenericObjectType>();
		sys.put("message", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		sys.put("country", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		sys.put("sunrise", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		sys.put("sunset", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType sysSchema = new MapObjectType(sys);

		Map<String, GenericObjectType> weather = new HashMap<String, GenericObjectType>();
		weather.put("id", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		weather.put("main", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		weather.put("description", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		weather.put("icon", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType weatherSchema = new MapObjectType(weather);
		List<GenericObjectType> weatherList = new LinkedList<GenericObjectType>();
		weatherList.add(weatherSchema);
		ListObjectType weatherListSchema = new ListObjectType(weatherList);

		Map<String, GenericObjectType> main = new HashMap<String, GenericObjectType>();
		main.put("temp", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		main.put("humidity", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		main.put("pressure", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		main.put("temp_min", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		main.put("temp_max", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		main.put("sea_level", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		main.put("grnd_level", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType mainSchema = new MapObjectType(main);

		Map<String, GenericObjectType> wind = new HashMap<String, GenericObjectType>();
		wind.put("speed", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		wind.put("gust", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		wind.put("deg", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType windSchema = new MapObjectType(wind);

		Map<String, GenericObjectType> rain = new HashMap<String, GenericObjectType>();
		rain.put("1h", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		rain.put("3h", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType rainSchema = new MapObjectType(rain);

		Map<String, GenericObjectType> clouds = new HashMap<String, GenericObjectType>();
		clouds.put("all", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType cloudsSchema = new MapObjectType(clouds);

		Map<String, GenericObjectType> result = new HashMap<String, GenericObjectType>();
		result.put("coord", coordSchema);
		result.put("sys", sysSchema);
		result.put("weather", weatherListSchema);
		result.put("base", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		result.put("main", mainSchema);
		result.put("wind", windSchema);
		result.put("rain", rainSchema);
		result.put("clouds", cloudsSchema);
		result.put("dt", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		result.put("id", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		result.put("name", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		result.put("cod", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		MapObjectType resultSchema = new MapObjectType(result);

		return resultSchema;
	}

	/**
	 * Test Translate null source.
	 */
	@Test()
	public void testTranslateWithSchema() {
		GenericObjectType genericObjectType = createOpenWeatherSchema();
		String url = "http://api.openweathermap.org/data/2.5/weather?q=Nuremberg,de";
		GenericEntity gv = translator.translate(new DataSource(url, genericObjectType));
		assertNotNull(gv);
	}

}
