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
package org.jvalue.ods.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

/**
 * The Class ApiRouter.
 */
public class ApiRouter implements Router {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/**
	 * Instantiates a new api router.
	 *
	 * @param routes the routes
	 */
	public ApiRouter(HashMap<String, Restlet> routes) {
		if (routes == null)
			throw new IllegalArgumentException("routes is null");
		
		this.routes = routes;
	}

	/* (non-Javadoc)
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {

		// creates a visualization of the API
		Restlet apiRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				for (String s : new TreeSet<String>(routes.keySet())) {

					message += s + "\n";
				}

				response.setEntity(message, MediaType.TEXT_PLAIN);

			}
		};

		routes.put("/api", apiRestlet);
		return routes;
	}

}
