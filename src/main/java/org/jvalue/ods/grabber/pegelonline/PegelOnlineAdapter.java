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
package org.jvalue.ods.grabber.pegelonline;

import java.io.IOException;
import java.util.List;

import org.jvalue.ods.grabber.generic.HttpJsonReader;
import org.jvalue.ods.grabber.pegelonline.data.Measurement;
import org.jvalue.ods.grabber.pegelonline.data.Station;
import org.jvalue.ods.grabber.pegelonline.data.Water;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PegelOnlineAdapter.
 */
public class PegelOnlineAdapter {

	/**
	 * Gets the stations.
	 * 
	 * @return the stations
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public List<Station> getStationData() throws JsonParseException,
			JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		List<Station> stationData = mapper.readValue(json,
				new TypeReference<List<Station>>() {
				});

		return stationData;
	}

	/**
	 * Gets the measurement of station.
	 *
	 * @param stationUUID the station uuid
	 * @param timeseriesShortname the timeseries shortname
	 * @param days the days
	 * @return the measurement of station
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Measurement> getMeasurementOfStation(String stationUUID,
			String timeseriesShortname, int days) throws JsonParseException,
			JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations/"
						+ stationUUID + "/" + timeseriesShortname
						+ "/measurements.json?start=P" + days + "D");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		List<Measurement> measurementData = mapper.readValue(json,
				new TypeReference<List<Measurement>>() {
				});
		return measurementData;

	}

	/**
	 * Gets the measurement data.
	 * 
	 * @return the measurement data
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public List<Water> getWaterData() throws JsonParseException,
			JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://www.pegelonline.wsv.de/webservices/rest-api/v2/waters.json?includeStations=true&includeTimeseries=true&includeCurrentMeasurement=true");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		List<Water> waterData = mapper.readValue(json,
				new TypeReference<List<Water>>() {
				});
		return waterData;
	}

}
