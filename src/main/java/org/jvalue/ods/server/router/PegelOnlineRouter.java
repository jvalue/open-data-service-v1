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
package org.jvalue.ods.server.router;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.server.restlet.ExecuteQueryRestlet;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.JsonNode;


class PegelOnlineRouter implements Router<Restlet> {

	private static final String CLIENT_ERROR_MSG = "Could not retrieve data. Try to update database via /pegelonline/update.";

	private DbAccessor<JsonNode> dbAccessor;

	public PegelOnlineRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new LinkedHashMap<String, Restlet>();

		// all stations
		routes.put(
				"/ods/de/pegelonline/stations", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor,
						"_design/pegelonline",
						"getAllStations")
					.errorMsg(CLIENT_ERROR_MSG)
					.build());

		// all stations flat
		routes.put(
				"/ods/de/pegelonline/stationsFlat", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor,
						"_design/pegelonline",
						"getAllStationsFlat")
					.errorMsg(CLIENT_ERROR_MSG)
					.build());

		// value types
		routes.put(
				"/ods/de/pegelonline/stations/$class",
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelonline",
						"getClassObject")
					.errorMsg(CLIENT_ERROR_MSG)
					.fetchAllDbEntries(false)
					.build());

		// value types id
		routes.put("/ods/de/pegelonline/stations/$class_id",
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelonline",
						"getClassObjectId")
					.errorMsg(CLIENT_ERROR_MSG)
					.fetchAllDbEntries(false)
					.build());

		// get single station
		routes.put(
				"/ods/de/pegelonline/stations/{station}",
				new ExecuteQueryRestlet.Builder(
						dbAccessor,
						"_design/pegelonline",
						"getSingleStation")
					.fetchAllDbEntries(false)
					.attributeName("station")
					.errorMsg(CLIENT_ERROR_MSG)
					.build());

		// metadata
		routes.put(
				"/ods/de/pegelonline/metadata", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor,
						"_design/pegelonline",
						"getMetadata")
					.fetchAllDbEntries(false)
					.errorMsg(CLIENT_ERROR_MSG)
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
