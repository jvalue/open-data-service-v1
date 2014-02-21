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
package integration.org.jvalue.ods.grabber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
import org.jvalue.ods.grabber.JsonGrabber;

/**
 * The Class JsonGrabberTest.
 */
public class JsonGrabberTest {

	/** The Test url. */
	private final String TestUrl = "http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true";

	/** The grabber. */
	private JsonGrabber grabber;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		grabber = new JsonGrabber();
		assertNotNull(grabber);
	}

	/**
	 * Test grab.
	 */
	@Test
	public void testGrab() {
		GenericValue gv = grabber.grab(new DataSource(TestUrl, null, null));
		assertNotNull(gv);
	}

	/**
	 * Test grab invalid source.
	 */
	@Test
	public void testGrabInvalidSource() {
		GenericValue gv = grabber.grab(new DataSource("invalidsource", null, null));
		assertNull(gv);
	}

	/**
	 * Test grab null source.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGrabNullSource() {
		grabber.grab(null);
	}
	
	/*
	{
   "coord":{
      "lon":11.07,
      "lat":49.45
   },
   "sys":{
      "message":0.0405,
      "country":"DE",
      "sunrise":1392963176,
      "sunset":1393001136
   },
   "weather":[
      {
         "id":500,
         "main":"Rain",
         "description":"light rain",
         "icon":"10d"
      }
   ],
   "base":"cmc stations",
   "main":{
      "temp":280.57,
      "humidity":94,
      "pressure":1009,
      "temp_min":279.82,
      "temp_max":281.15
   },
   "wind":{
      "speed":1.54,
      "gust":3.6,
      "deg":186
   },
   "rain":{
      "1h":0.33
   },
   "clouds":{
      "all":92
   },
   "dt":1392984565,
   "id":2861650,
   "name":"Nuremberg",
   "cod":200
}
	 */
	
	/**
	 * Creates the open weather schema.
	 *
	 * @return the schema
	 */
	private static Schema createOpenWeatherSchema() {
		Map<String, Schema> coord = new HashMap<String, Schema>();
		coord.put("lon", new NumberSchema());
		coord.put("lat", new NumberSchema());
		MapSchema coordSchema = new MapSchema(coord);
		
		Map<String, Schema> sys = new HashMap<String, Schema>();
		sys.put("message", new NumberSchema());
		sys.put("country", new StringSchema());
		sys.put("sunrise", new NumberSchema());
		sys.put("sunset", new NumberSchema());
		MapSchema sysSchema = new MapSchema(sys);
		
		Map<String, Schema> weather = new HashMap<String, Schema>();
		weather.put("id", new NumberSchema());
		weather.put("main", new StringSchema());
		weather.put("description", new StringSchema());
		weather.put("icon", new StringSchema());
		MapSchema weatherSchema = new MapSchema(weather);
		List<Schema> weatherList = new LinkedList<Schema>();
		weatherList.add(weatherSchema);
		ListSchema weatherListSchema = new ListSchema(weatherList);
		
		Map<String, Schema> main = new HashMap<String, Schema>();
		main.put("temp", new NumberSchema());
		main.put("humidity", new NumberSchema());
		main.put("pressure", new NumberSchema());
		main.put("temp_min", new NumberSchema());
		main.put("temp_max", new NumberSchema());
		MapSchema mainSchema = new MapSchema(main);
		
		Map<String, Schema> wind = new HashMap<String, Schema>();
		wind.put("speed", new NumberSchema());
		wind.put("gust", new NumberSchema());
		wind.put("deg", new NumberSchema());		
		MapSchema windSchema = new MapSchema(wind);
		
		Map<String, Schema> rain = new HashMap<String, Schema>();
		rain.put("1h", new NumberSchema());			
		MapSchema rainSchema = new MapSchema(rain);
		
		Map<String, Schema> clouds = new HashMap<String, Schema>();
		clouds.put("all", new NumberSchema());			
		MapSchema cloudsSchema = new MapSchema(clouds);
		
		
		Map<String, Schema> result = new HashMap<String, Schema>();
		result.put("coord", coordSchema);
		result.put("sys", sysSchema);
		result.put("weather", weatherListSchema);
		result.put("base", new StringSchema());
		result.put("main", mainSchema);
		result.put("wind", windSchema);
		result.put("rain", rainSchema);
		result.put("clouds", cloudsSchema);
		result.put("dt", new NumberSchema());
		result.put("id", new NumberSchema());
		result.put("name", new StringSchema());
		result.put("cod", new NumberSchema());
		MapSchema resultSchema = new MapSchema(result);
		
		return resultSchema;
	}
	/**
	 * Test grab null source.
	 */
	@Test()
	public void testGrabWithSchema() {
		Schema schema = createOpenWeatherSchema();
		String url = "http://api.openweathermap.org/data/2.5/weather?q=Nuremberg,de";
		GenericValue gv = grabber.grab(new DataSource(url, schema, null));
		assertNotNull(gv);
	}
	
	
	
	
	
}
