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
package org.jvalue.ods.server.openstreetmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.Router;
import org.jvalue.ods.translator.TranslatorFactory;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class OverpassRouter.
 */
public class OverpassRouter implements Router<Restlet> {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/**
	 * Gets the routes.
	 * 
	 * @return the routes
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		Restlet overpassLocationRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";

				GenericEntity ret = null;
				try {
					ret = TranslatorFactory.getJsonTranslator().translate(new DummyDataSource(
							"ru-rambler-osm-overpass",
							"http://overpass.osm.rambler.ru/cgi/interpreter?data=[out:json];node[name%3D"
									+ (String) request.getAttributes().get(
											"location") + "]%3Bout%3B", null, null, null, null));
					ObjectMapper mapper = new ObjectMapper();
					message += mapper.writeValueAsString(ret);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					message += "Station not found: "
							+ (String) request.getAttributes().get("location");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}

		};

		Restlet metadataRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				try {
					ObjectMapper mapper = new ObjectMapper();

					String message = mapper
							.writeValueAsString(new JacksonMetaData(
									"org-openstreetmap-overpass",
									"overpass",
									"OpenStreetMap Community",
									"http://www.openstreetmap.org",
									"The Overpass API is a read-only API that serves up custom selected parts of the OSM map data. It acts as a database over the web: the client sends a query to the API and gets back the data set that corresponds to the query.",
									"http://overpass.osm.rambler.ru",
									"http://www.openstreetmap.org/copyright"));

					response.setEntity(message, MediaType.APPLICATION_JSON);
				} catch (JsonProcessingException ex) {
					response.setEntity("OSM Metadata not found.",
							MediaType.TEXT_PLAIN);
					Logging.error(this.getClass(), "OSM Metadata not found.");
					System.err.println("OSM Metadata not found.");
				}
			}
		};

		routes.put("/services/de/overpass/{location}", overpassLocationRestlet);
		routes.put("/services/de/overpass/metadata", metadataRestlet);

		return routes;
	}
}
