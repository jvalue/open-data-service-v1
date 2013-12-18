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

package org.jvalue.ods.main;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.adapter.pegelonline.PegelOnlineAdapter;
import org.jvalue.ods.adapter.pegelonline.data.*;
import org.jvalue.ods.inserter.*;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;


/**
 * The Class Main.
 */
public class Main extends ServerResource {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		initializeRestlet();
		
		insertPegelOnlineStationsIntoDatabase();		

		//printPegelOnlineStations();
	}
	
	/**
	 * Initialize restlet.
	 *
	 * @throws Exception the exception
	 */
	private static void initializeRestlet() throws Exception
	{
		// Create a new Restlet component and add a HTTP server connector to it  
	    Component component = new Component();  
	    component.getServers().add(Protocol.HTTP, 8182);  
	  
	    // Then attach it to the local host  
	    component.getDefaultHost().attach("/", Main.class);  
	  
	    // Now, let's start the component!  
	    // Note that the HTTP server connector is also automatically started.  
	    component.start(); 
	}
	
//	private static void printPegelOnlineStations() throws JsonParseException,
//			JsonMappingException, IOException {
//		PegelOnlineAdapter pegelOnlineAdapter = new PegelOnlineAdapter();
//		List<Station> stationData = pegelOnlineAdapter.getStationData();
//		for (Station s : stationData) {
//
//			System.out.println("Id:" + s.getUuid() + "\t" + "Pegelname: "
//					+ s.getLongname() + "\t" + "Fluss-KM: " + s.getKm() + "\t"
//					+ "Gewässer: " + s.getLongname());
//
//			for (Timeseries t : s.getTimeseries()) {
//				String comment = "";
//				Comment c = t.getComment();
//				if (c != null)
//					comment = c.getLongDescription();
//
//				System.out.println("\t" + t.getLongname() + ": " + comment);
//
//				List<Measurement> measurementData = pegelOnlineAdapter
//						.getMeasurementOfStation(s.getUuid(), t.getShortname());
//				for (Measurement m : measurementData) {
//					System.out.println("\t\t" + m.getTimestamp() + "\t"
//							+ m.getValue() + t.getUnit());
//				}
//			}
//			System.out.println();
//		}
//	}

	/**
 * Insert pegel online stations into database.
 *
 * @throws JsonParseException the json parse exception
 * @throws JsonMappingException the json mapping exception
 * @throws IOException Signals that an I/O exception has occurred.
 */
private static void insertPegelOnlineStationsIntoDatabase() throws JsonParseException, JsonMappingException, IOException {
		PegelOnlineAdapter pegelOnlineAdapter = new PegelOnlineAdapter();
		List<Station> stationData = pegelOnlineAdapter.getStationData();
		List<CouchDbDocument> list = new LinkedList<CouchDbDocument>();
		for (Station s: stationData)
		{
			list.add(s);
		}
		
		Inserter inserter = new CouchDbInserter("open-data-service", list);
		inserter.insert();		
	}
	

	/**
	 * Do get.
	 *
	 * @return the list
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Get("json")
	public List<Station> doGet() throws JsonParseException, JsonMappingException, IOException{
		PegelOnlineAdapter pegelOnlineAdapter = new PegelOnlineAdapter();
		List<Station> stationData = pegelOnlineAdapter.getStationData();	    
		
		return stationData;
//		// Print the requested URI path  
//	    return "Resource URI  : " + getReference() + '\n' + "Root URI      : "  
//	            + getRootRef() + '\n' + "Routed part   : "  
//	            + getReference().getBaseRef() + '\n' + "Remaining part: "  
//	            + getReference().getRemainingPart();  
	}  
	

}
