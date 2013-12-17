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
//package org.jvalue.ods.inserter.pegelonline;
//
//
//import java.util.List;
//
//import org.jvalue.ods.adapter.data.Station;
//
//
///**
// * The Class PegelOnlineDBInserter.
// */
//public class PegelOnlineDBInserter implements PegelOnlineInserter {
//
//	/** list of weather stations and their measurement data */
//	private List<Station> stationData;
//
//	/** The morphia. */
//	private Morphia morphia;
//
//	/** The datastore. */
//	private Datastore datastore = null;
//
//	/**
//	 * Instantiates a new pegel online db inserter.
//	 * 
//	 * @param stationData
//	 *            the station data
//	 */
//	public PegelOnlineDBInserter(List<Station> stationData) {
//		this.stationData = stationData;
//
//		initMorphia();
//
//	}
//
//	// init morphia for object persistence
//	private void initMorphia() {
//		morphia = new Morphia();
//		//datastore = morphia.createDatastore(new MongoClient(), "pegelonline");
//		
//		// package is annotated
//		morphia.mapPackage("common.data.pegelonline");
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see inserter.pegelonline.PegelOnlineInserter#insert()
//	 */
//	public void insert() {
//
//		// insert each station into mongodb
//		for (Station s : stationData) {
//
//			Key<Station> savedStation = datastore.save(s);
//			System.out.println("Insert into db: " + savedStation.getId());
//
//		}
//
//	}
//
//}
