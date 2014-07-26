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

import org.junit.Test;
import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.valuetypes.AllowedValueTypes;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.ListComplexValueType;
import org.jvalue.ods.data.valuetypes.MapComplexValueType;
import org.jvalue.ods.grabber.GrabberFactory;
import org.jvalue.ods.translator.TranslatorFactory;

import com.fasterxml.jackson.databind.JsonNode;


public class JsonTranslatorTest {

	private final String testUrl = "http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true";


	@Test
	public void testTranslate() {
		JsonNode jsonNode = GrabberFactory.getJsonGrabber(DummyDataSource.newInstance("testUrl", testUrl)).filter(null);
		GenericEntity gv = TranslatorFactory.getJsonTranslator().filter(jsonNode);
		assertNotNull(gv);
	}

	@Test
	public void testTranslateInvalidSource() {
		JsonNode jsonNode = GrabberFactory.getJsonGrabber(DummyDataSource.newInstance("invalidSource", "invalidSource")).filter(null);
		assertNull(jsonNode);
	}

	
	private static GenericValueType createOpenWeatherSchema() {
		Map<String, GenericValueType> coord = new HashMap<String, GenericValueType>();
		coord.put("lon", AllowedValueTypes.VALUETYPE_NUMBER);
		coord.put("lat", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType coordSchema = new MapComplexValueType(coord);

		Map<String, GenericValueType> sys = new HashMap<String, GenericValueType>();
		sys.put("message", AllowedValueTypes.VALUETYPE_NUMBER);
		sys.put("country", AllowedValueTypes.VALUETYPE_STRING);
		sys.put("sunrise", AllowedValueTypes.VALUETYPE_NUMBER);
		sys.put("sunset", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType sysSchema = new MapComplexValueType(sys);

		Map<String, GenericValueType> weather = new HashMap<String, GenericValueType>();
		weather.put("id", AllowedValueTypes.VALUETYPE_NUMBER);
		weather.put("main", AllowedValueTypes.VALUETYPE_STRING);
		weather.put("description", AllowedValueTypes.VALUETYPE_STRING);
		weather.put("icon", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType weatherSchema = new MapComplexValueType(weather);
		List<GenericValueType> weatherList = new LinkedList<GenericValueType>();
		weatherList.add(weatherSchema);
		ListComplexValueType weatherListSchema = new ListComplexValueType(weatherList);

		Map<String, GenericValueType> main = new HashMap<String, GenericValueType>();
		main.put("temp", AllowedValueTypes.VALUETYPE_NUMBER);
		main.put("humidity", AllowedValueTypes.VALUETYPE_NUMBER);
		main.put("pressure", AllowedValueTypes.VALUETYPE_NUMBER);
		main.put("temp_min", AllowedValueTypes.VALUETYPE_NUMBER);
		main.put("temp_max", AllowedValueTypes.VALUETYPE_NUMBER);
		main.put("sea_level", AllowedValueTypes.VALUETYPE_NUMBER);
		main.put("grnd_level", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType mainSchema = new MapComplexValueType(main);

		Map<String, GenericValueType> wind = new HashMap<String, GenericValueType>();
		wind.put("speed", AllowedValueTypes.VALUETYPE_NUMBER);
		wind.put("gust", AllowedValueTypes.VALUETYPE_NUMBER);
		wind.put("deg", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType windSchema = new MapComplexValueType(wind);

		Map<String, GenericValueType> rain = new HashMap<String, GenericValueType>();
		rain.put("1h", AllowedValueTypes.VALUETYPE_NUMBER);
		rain.put("3h", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType rainSchema = new MapComplexValueType(rain);

		Map<String, GenericValueType> clouds = new HashMap<String, GenericValueType>();
		clouds.put("all", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType cloudsSchema = new MapComplexValueType(clouds);

		Map<String, GenericValueType> result = new HashMap<String, GenericValueType>();
		result.put("coord", coordSchema);
		result.put("sys", sysSchema);
		result.put("weather", weatherListSchema);
		result.put("base", AllowedValueTypes.VALUETYPE_STRING);
		result.put("main", mainSchema);
		result.put("wind", windSchema);
		result.put("rain", rainSchema);
		result.put("clouds", cloudsSchema);
		result.put("dt", AllowedValueTypes.VALUETYPE_NUMBER);
		result.put("id", AllowedValueTypes.VALUETYPE_NUMBER);
		result.put("name", AllowedValueTypes.VALUETYPE_STRING);
		result.put("cod", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType resultSchema = new MapComplexValueType(result);

		return resultSchema;
	}

	/**
	 * Test Translate null source.
	 */
//	@Test()
//	public void testTranslateWithSchema() {		
//		GenericValueType genericObjectType = createOpenWeatherSchema();
//		String url = "http://api.openweathermap.org/data/2.5/weather?q=Nuremberg,de";
//		GenericEntity gv = translator.translate(new DataSource("org-openweather-api", url, genericObjectType, null, null, null) {});
//		assertNotNull(gv);
//	}
//TODO
}
