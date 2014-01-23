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
package integration.org.jvalue.ods.grabber.pegelonline;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.grabber.pegelonline.PegelOnlineAdapter;
import org.jvalue.ods.grabber.pegelonline.data.Measurement;
import org.jvalue.ods.grabber.pegelonline.data.Station;
import org.jvalue.ods.grabber.pegelonline.data.Timeseries;
import org.jvalue.ods.grabber.pegelonline.data.Water;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * The Class PegelOnlineAdapterTest.
 */
public class PegelOnlineAdapterTest {

	/** The adapter. */
	PegelOnlineAdapter adapter;

	/**
	 * Initialize.
	 */
	@Before
	public void initialize() {
		adapter = new PegelOnlineAdapter();
		assertNotNull(adapter);
	}

	/**
	 * Gets the measurement data test.
	 * 
	 * @return the measurement data test
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetWaterData() throws JsonParseException,
			JsonMappingException, IOException {
		List<Water> waterData = adapter.getWaterData();
		assertNotNull(waterData);
		for (Water measurement : waterData) {
			assertNotNull(measurement);
		}
	}

	/**
	 * Gets the stations test.
	 * 
	 * @return the stations test
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetStationData() throws JsonParseException,
			JsonMappingException, IOException {
		List<Station> stationData = adapter.getStationData();
		assertNotNull(stationData);
		for (Station station : stationData) {
			assertNotNull(station);
		}
	}
	
	
	/**
	 * Test get measurement of station.
	 *
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void testGetMeasurementOfStation() throws JsonParseException, JsonMappingException, IOException
	{
		List<Station> stationData = adapter.getStationData();
		assertNotNull(stationData);
		
		Station s = stationData.get(0);
		if (s != null)
		{
			Timeseries t = s.getTimeseries().get(0);
			if (t != null)
			{
				String timeseriesShortname = t.getShortname();
				List<Measurement> measurementData = adapter.getMeasurementOfStation(s.getUuid(), timeseriesShortname, 1);
				assertNotNull(measurementData);
				for (Measurement m: measurementData)
				{
					assertNotNull(m);
				}
			}
		}
	}
		
		/**
		 * Test get measurement of station with null uuid.
		 *
		 * @throws JsonParseException the json parse exception
		 * @throws JsonMappingException the json mapping exception
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Test(expected = IOException.class)
		public void testGetMeasurementOfStationWithNullUUID() throws JsonParseException, JsonMappingException, IOException
		{
			List<Station> stationData = adapter.getStationData();
			assertNotNull(stationData);
			
			Station s = stationData.get(0);
			if (s != null)
			{
				Timeseries t = s.getTimeseries().get(0);
				if (t != null)
				{
					String timeseriesShortname = t.getShortname();
					adapter.getMeasurementOfStation(null, timeseriesShortname, 1);
					
				}
			}
		}
		
		/**
		 * Test get measurement of station with empty uuid.
		 *
		 * @throws JsonParseException the json parse exception
		 * @throws JsonMappingException the json mapping exception
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Test(expected = IOException.class)
		public void testGetMeasurementOfStationWithEmptyUUID() throws JsonParseException, JsonMappingException, IOException
		{
			List<Station> stationData = adapter.getStationData();
			assertNotNull(stationData);
			
			Station s = stationData.get(0);
			if (s != null)
			{
				Timeseries t = s.getTimeseries().get(0);
				if (t != null)
				{
					String timeseriesShortname = t.getShortname();
					adapter.getMeasurementOfStation("", timeseriesShortname, 1);
					
				}
			}
		}
		
		
		/**
		 * Test get measurement of station with null time series short name.
		 *
		 * @throws JsonParseException the json parse exception
		 * @throws JsonMappingException the json mapping exception
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Test(expected = IOException.class)
		public void testGetMeasurementOfStationWithNullTimeSeriesShortName() throws JsonParseException, JsonMappingException, IOException
		{
			List<Station> stationData = adapter.getStationData();
			assertNotNull(stationData);
			
			Station s = stationData.get(0);
			if (s != null)
			{
				Timeseries t = s.getTimeseries().get(0);
				if (t != null)
				{
					List<Measurement> measurementData = adapter.getMeasurementOfStation(s.getUuid(), null, 1);
					assertNotNull(measurementData);
					for (Measurement m: measurementData)
					{
						assertNotNull(m);
					}
				}
			}
		}

		/**
		 * Test get measurement of station with empty time series short name.
		 *
		 * @throws JsonParseException the json parse exception
		 * @throws JsonMappingException the json mapping exception
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		@Test(expected = IOException.class)
		public void testGetMeasurementOfStationWithEmptyTimeSeriesShortName() throws JsonParseException, JsonMappingException, IOException
		{
			List<Station> stationData = adapter.getStationData();
			assertNotNull(stationData);
			
			Station s = stationData.get(0);
			if (s != null)
			{
				Timeseries t = s.getTimeseries().get(0);
				if (t != null)
				{
					List<Measurement> measurementData = adapter.getMeasurementOfStation(s.getUuid(), "", 1);
					assertNotNull(measurementData);
					for (Measurement m: measurementData)
					{
						assertNotNull(m);
					}
				}
			}
		}

}
