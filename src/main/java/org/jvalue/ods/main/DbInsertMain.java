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
package org.jvalue.ods.main;

import java.io.IOException;
import java.util.List;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.adapter.pegelonline.PegelOnlineAdapter;
import org.jvalue.ods.adapter.pegelonline.data.*;
import org.jvalue.ods.db.CouchDbAdapter;
import org.jvalue.ods.db.DbAdapter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * The Class DbInsertMain.
 */
public class DbInsertMain {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws JsonParseException,
			JsonMappingException, IOException {
		insertPegelOnlineStationsIntoDatabase();
	}

	/**
	 * Insert pegel online stations into database.
	 * 
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void insertPegelOnlineStationsIntoDatabase()
			throws JsonParseException, JsonMappingException, IOException {
		PegelOnlineAdapter pegelOnlineAdapter = new PegelOnlineAdapter();
		List<Station> stationData = pegelOnlineAdapter.getStationData();

		// for (Station s : stationData) {
		// List<Measurement> measurementData =
		// pegelOnlineAdapter.getMeasurementOfStation(s.getUuid(),
		// s.getTimeseries().get(0).getShortname(), 1);
		// s.setOldMeasurements(measurementData);
		// // for (Timeseries t : s.getTimeseries()) {
		// // List<Measurement> measurementData =
		// pegelOnlineAdapter.getMeasurementOfStation(s.getUuid(),
		// t.getShortname(), 100);
		// // s.setOldMeasurements(measurementData);
		// // }
		// }

		DbAdapter<CouchDbDocument> adapter = new CouchDbAdapter(
				"open-data-service");
		adapter.insert(new PegelOnlineData(stationData));
	}

}
