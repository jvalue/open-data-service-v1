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

import org.jvalue.ods.administration.AdministrationRouter;
import org.jvalue.ods.server.ApiRouter;
import org.jvalue.ods.server.OdsRouter;
import org.jvalue.ods.server.openstreetmap.NominatimRouter;
import org.jvalue.ods.server.openstreetmap.OsmRouter;
import org.jvalue.ods.server.openstreetmap.OverpassRouter;
import org.jvalue.ods.server.pegelonline.PegelOnlineRouter;
import org.jvalue.ods.server.pegelportalMv.PegelPortalMvRouter;
import org.jvalue.ods.server.routes.RoutesRouter;
import org.restlet.Restlet;

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
	public static ApiRouter createApiRouter(HashMap<String, Restlet> routes) {
		return new ApiRouter(routes);
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the nominatim router
	 */
	public static NominatimRouter createNominatimRouter() {
		return new NominatimRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the osm router
	 */
	public static OsmRouter createOsmRouter() {
		return new OsmRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the overpass router
	 */
	public static OverpassRouter createOverpassRouter() {
		return new OverpassRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the pegel online router
	 */
	public static PegelOnlineRouter createPegelOnlineRouter() {
		return new PegelOnlineRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the mv pegel portal router 
	 */
	public static PegelPortalMvRouter createPegelPortalMvRouter() {
		return new PegelPortalMvRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the router< restlet>
	 */
	public static Router<Restlet> createRoutesRouter() {
		return new RoutesRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the router< restlet>
	 */
	public static Router<Restlet> createAdministrationRouter() {
		return new AdministrationRouter();
	}

	/**
	 * Creates a new Router object.
	 * 
	 * @return the router< restlet>
	 */
	public static org.jvalue.ods.main.Router<Restlet> createOdsRouter() {
		return new OdsRouter();
	}
}
