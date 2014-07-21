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


class PegelPortalMvRouter implements Router<Restlet> {
	
	private DbAccessor<JsonNode> dbAccessor;

	public PegelPortalMvRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");
	}

	
	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new LinkedHashMap<String, Restlet>();

		// all stations
		routes.put(
				"/ods/de/pegelportal-mv/stations", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getAllStations")
					.build());

		// all stations flat
		routes.put(
				"/ods/de/pegelportal-mv/stationsFlat", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getAllStationsFlat")
					.build());

		// value types 
		routes.put(
				"/ods/de/pegelportal-mv/stations/$class", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getClassObject")
					.fetchAllDbEntries(false)
					.build());

		// value types id
		routes.put(
				"/ods/de/pegelportal-mv/stations/$class_id",
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getClassObjectId")
					.fetchAllDbEntries(false)
					.build());

		// get single station
		routes.put(
				"/ods/de/pegelportal-mv/stations/{station}", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getSingleStation")
					.fetchAllDbEntries(false)
					.attributeName("station")
					.build());

		// meta data
		routes.put(
				"/ods/de/pegelportal-mv/metadata", 
				new ExecuteQueryRestlet.Builder(
						dbAccessor, 
						"_design/pegelportal-mv", 
						"getMetadata")
					.fetchAllDbEntries(false)
					.build());
		return routes;
	}


	public DbAccessor<JsonNode> getDbAccessor() {
		return dbAccessor;
	}


	public void setDbAccessor(DbAccessor<JsonNode> dbAccessor) {
		this.dbAccessor = dbAccessor;
	}

}
