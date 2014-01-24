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
package org.jvalue.ods.server.nominatim;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.opensteetmap.nominatim.NominatimQueryResult;
import org.jvalue.ods.data.opensteetmap.nominatim.NominatimReverseQueryResult;
import org.jvalue.ods.grabber.openstreetmap.nominatim.NominatimGrabber;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.server.Router;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class NominatimRouter.
 */
public class NominatimRouter implements Router {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/**
	 * Instantiates a new nominatim router.
	 */
	public NominatimRouter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		Restlet nominatimRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<NominatimQueryResult> sd = null;
					try {
						sd = new NominatimGrabber()
								.getNominatimData((String) request
										.getAttributes().get("location"));
					} catch (RuntimeException e) {
						return;
					}

					ObjectMapper mapper = new ObjectMapper();
					message += mapper.writeValueAsString(sd);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
				}

				if (!message.equals("")) {
					response.setEntity(message, MediaType.APPLICATION_JSON);
				} else {
					response.setEntity("No stations found.",
							MediaType.TEXT_PLAIN);
				}
			}

		};

		Restlet reverseNominatimRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					NominatimReverseQueryResult ret = null;
					try {
						ret = new NominatimGrabber()
								.getReverseNominatimData((String) request
										.getAttributes().get("coordinates"));
					} catch (RuntimeException e) {
						return;
					}

					ObjectMapper mapper = new ObjectMapper();
					message += mapper.writeValueAsString(ret);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
				}

				if (!message.equals("")) {
					response.setEntity(message, MediaType.APPLICATION_JSON);
				} else {
					response.setEntity("No stations found.",
							MediaType.TEXT_PLAIN);
				}
			}

		};

		routes.put("/nominatim/coordinates/{location}", nominatimRestlet);
		routes.put("/nominatim/reverse{coordinates}", reverseNominatimRestlet);

		return routes;
	}

}
