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

import java.util.HashMap;

import org.jvalue.ods.administration.AdministrationRouter;
import org.restlet.Restlet;


public class RouterFactory {

	private RouterFactory() { }


	public static Router<Restlet> createApiRouter(HashMap<String, Restlet> routes) {
		return new ApiRouter(routes);
	}


	public static Router<Restlet> createNominatimRouter() {
		return new NominatimRouter();
	}


	public static Router<Restlet> createOsmRouter() {
		return new OsmRouter();
	}


	public static Router<Restlet> createOverpassRouter() {
		return new OverpassRouter();
	}


	public static Router<Restlet> createPegelOnlineRouter() {
		return new PegelOnlineRouter();
	}


	public static Router<Restlet> createPegelPortalMvRouter() {
		return new PegelPortalMvRouter();
	}


	public static Router<Restlet> createRoutesRouter() {
		return new RoutesRouter();
	}


	public static Router<Restlet> createAdministrationRouter() {
		return new AdministrationRouter();
	}


	public static Router<Restlet> createOdsRouter() {
		return new OdsRouter();
	}


	public static Router<Restlet> createNotificationRouter() {
		return new NotificationRouter();
	}
}
