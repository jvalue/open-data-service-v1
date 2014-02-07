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

import java.util.HashMap;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.server.ApiRouter;
import org.jvalue.ods.server.openstreetmap.NominatimRouter;
import org.jvalue.ods.server.openstreetmap.OsmRouter;
import org.jvalue.ods.server.openstreetmap.OverpassRouter;
import org.jvalue.ods.server.pegelonline.PegelOnlineRouter;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A factory for creating Router objects.
 */
public class RouterFactory {

	/**
	 * Creates a new Router object.
	 * 
	 * @param routes
	 *            the routes
	 * @return the api router
	 */
	public ApiRouter createApiRouter(HashMap<String, Restlet> routes) {
		return new ApiRouter(routes);
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the nominatim router
	 */
	public NominatimRouter createNominatimRouter() {
		return new NominatimRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @param dbAccessor
	 *            the db accessor
	 * @return the osm router
	 */
	public OsmRouter createOsmRouter(DbAccessor<JsonNode> dbAccessor) {
		return new OsmRouter(dbAccessor);
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the overpass router
	 */
	public OverpassRouter createOverpassRouter() {
		return new OverpassRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @param dbAccessor
	 *            the db accessor
	 * @return the pegel online router
	 */
	public PegelOnlineRouter createPegelOnlineRouter(
			DbAccessor<JsonNode> dbAccessor) {
		return new PegelOnlineRouter(dbAccessor);
	}
}
