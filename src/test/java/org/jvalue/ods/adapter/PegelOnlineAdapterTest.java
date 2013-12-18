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
package org.jvalue.ods.adapter;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.jvalue.ods.adapter.pegelonline.PegelOnlineAdapter;
import org.jvalue.ods.adapter.pegelonline.data.Station;
import org.jvalue.ods.adapter.pegelonline.data.Water;

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
	public void getWaterDataTest() throws JsonParseException,
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
	public void getStationDataTest() throws JsonParseException,
			JsonMappingException, IOException {
		List<Station> stationData = adapter.getStationData();
		assertNotNull(stationData);
		for (Station station : stationData) {
			assertNotNull(station);
		}

	}

}
