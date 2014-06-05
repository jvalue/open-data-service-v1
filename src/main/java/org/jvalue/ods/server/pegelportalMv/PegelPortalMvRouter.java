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
package org.jvalue.ods.server.pegelportalMv;

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.main.Router;
import org.jvalue.ods.server.restlet.ExecuteQueryRestlet;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.JsonNode;


public class PegelPortalMvRouter implements Router<Restlet> {
	
	private final static String CLIENT_ERROR_MSG = "Could not retrieve data. Try to update database via /pegelportal-mv/update.";

	private DbAccessor<JsonNode> dbAccessor;

	public PegelPortalMvRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		// all stations
		routes.put(
				"/ods/de/pegelportal-mv/stations", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getAllStations")
					.customErrorMsg(CLIENT_ERROR_MSG)
					.build());

		// all stations flat
		routes.put(
				"/ods/de/pegelportal-mv/stationsFlat", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getAllStationsFlat")
					.customErrorMsg(CLIENT_ERROR_MSG)
					.build());

		/*
		// get single station
		routes.put(
				"/ods/de/pegelportal-mv/stations/{station}", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getAllStationsFlat")
					.customErrorMsg(CLIENT_ERROR_MSG)
					.build());
					*/

		// meta data
		routes.put(
				"/ods/de/pegelportal-mv/metadata", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getMetadata")
					.customErrorMsg(CLIENT_ERROR_MSG)
					.fetchAllDbEntries(false)
					.build());

		// value types 
		routes.put(
				"/ods/de/pegelportal-mv/stations/$class", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getClassObject")
					.customErrorMsg(CLIENT_ERROR_MSG)
					.fetchAllDbEntries(false)
					.build());
		return routes;
	}

	/**
	 * Gets the db accessor.
	 * 
	 * @return the db accessor
	 */
	public DbAccessor<JsonNode> getDbAccessor() {
		return dbAccessor;
	}

	/**
	 * Sets the db accessor.
	 * 
	 * @param dbAccessor
	 *            the new db accessor
	 */
	public void setDbAccessor(DbAccessor<JsonNode> dbAccessor) {
		this.dbAccessor = dbAccessor;
	}

}
