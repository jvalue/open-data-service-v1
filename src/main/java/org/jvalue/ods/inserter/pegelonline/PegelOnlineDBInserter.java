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
package org.jvalue.ods.inserter.pegelonline;

import java.util.List;

import org.ektorp.*;
import org.ektorp.http.*;
import org.ektorp.impl.*;
import org.jvalue.ods.adapter.pegelonline.data.Station;

/**
 * The Class PegelOnlineDBInserter.
 */
public class PegelOnlineDBInserter implements PegelOnlineInserter {

	private final String DATABASE_NAME = "open-data-service";
	private CouchDbConnector db;
	
	/** list of weather stations and their measurement data */
	private List<Station> stationData;

	/**
	 * Instantiates a new pegel online db inserter.
	 * 
	 * @param stationData
	 *            the station data
	 */
	public PegelOnlineDBInserter(List<Station> stationData) {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
		if (!dbInstance.checkIfDbExists(DATABASE_NAME))
			dbInstance.createDatabase(DATABASE_NAME);		
		this.db = new StdCouchDbConnector(DATABASE_NAME, dbInstance);
		
		this.stationData = stationData;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see inserter.pegelonline.PegelOnlineInserter#insert()
	 */
	public void insert() {
		// insert each station into couchdb
		for (Station s : stationData) {	
			db.create(s);
			System.out.println("Insert into db: " + s.getId());
		}
	}

}
