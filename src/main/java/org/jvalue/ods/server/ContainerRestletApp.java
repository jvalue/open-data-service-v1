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
package org.jvalue.ods.server;

import org.jvalue.ods.utils.Log;
import org.jvalue.ods.server.restlet.DefaultRestlet;
import org.jvalue.ods.server.router.RouterFactory;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The Class ContainerRestletApp.
 */
public class ContainerRestletApp extends Application {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public Restlet createInboundRoot() {
		org.jvalue.ods.server.router.Router<Restlet> odsRouter = RouterFactory
				.createOdsRouter();
		org.jvalue.ods.server.router.Router<Restlet> poRouter = RouterFactory
				.createPegelOnlineRouter();
		org.jvalue.ods.server.router.Router<Restlet> ppRouter = RouterFactory
				.createPegelPortalMvRouter();
		org.jvalue.ods.server.router.Router<Restlet> noRouter = RouterFactory
				.createNominatimRouter();
		org.jvalue.ods.server.router.Router<Restlet> ovRouter = RouterFactory
				.createOverpassRouter();
		org.jvalue.ods.server.router.Router<Restlet> osmRouter = RouterFactory
				.createOsmRouter();
		org.jvalue.ods.server.router.Router<Restlet> routesRouter = RouterFactory
				.createRoutesRouter();

		HashMap<String, Restlet> combinedRouter = new LinkedHashMap<String, Restlet>();
		combinedRouter.putAll(odsRouter.getRoutes());
		combinedRouter.putAll(poRouter.getRoutes());
		combinedRouter.putAll(ppRouter.getRoutes());
		combinedRouter.putAll(noRouter.getRoutes());
		combinedRouter.putAll(ovRouter.getRoutes());
		combinedRouter.putAll(osmRouter.getRoutes());
		combinedRouter.putAll(routesRouter.getRoutes());

		// must be last router, generates api output
		org.jvalue.ods.server.router.Router<Restlet> apiRouter = RouterFactory
				.createApiRouter(combinedRouter.keySet());
		combinedRouter.putAll(apiRouter.getRoutes());

		if (combinedRouter.isEmpty()) {
			String errorMessage = "routes are empty";
			Log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		// Create a root router
		Router router = new Router(getContext());

		router.attachDefault(new DefaultRestlet());

		// Get Map in Set interface to get key and value
		Set<Entry<String, Restlet>> s = combinedRouter.entrySet();

		// Move next key and value of Map by iterator
		Iterator<Entry<String, Restlet>> it = s.iterator();

		while (it.hasNext()) {
			Entry<String, Restlet> m = it.next();
			router.attach(m.getKey(), m.getValue());
		}

		return router;
	}

}
