package org.jvalue.ods.main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jvalue.ods.adapter.pegelonline.PegelOnlineAdapter;
import org.jvalue.ods.adapter.pegelonline.data.PegelOnlineData;
import org.jvalue.ods.adapter.pegelonline.data.Station;
import org.jvalue.ods.inserter.CouchDbInserter;
import org.jvalue.ods.inserter.Inserter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DbInsertMain {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
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
		
//TODO: Write Measurements to DB
//		for (Station s : stationData) {
//			for (Timeseries t : s.getTimeseries()) {
//				List<Measurement> measurementData = pegelOnlineAdapter
//						.getMeasurementOfStation(s.getUuid(), t.getShortname());
//				
//				for (Measurement m : measurementData) {
//					//...
//				}
//			}
//		}		
		
		
		List<Station> stations = new LinkedList<Station>();
		for (Station s : stationData) {
			stations.add(s);
		}
		
		

		Inserter inserter = new CouchDbInserter("open-data-service", new PegelOnlineData(stations));
		inserter.insert();
	}
	
	
	

}
