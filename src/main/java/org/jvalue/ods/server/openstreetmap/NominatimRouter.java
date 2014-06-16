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
import org.jvalue.ods.translator.JsonTranslator;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NominatimRouter implements Router<Restlet> {

	private HashMap<String, Restlet> routes;

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

					GenericEntity ret = null;

					ret = JsonTranslator.INSTANCE
							.translate(new DummyDataSource(
									"org-nominatim-openstreetmap",
									"http://nominatim.openstreetmap.org/search?q="
											+ (String) request.getAttributes()
													.get("location")
											+ "&format=json", null, null, null, null));

					ObjectMapper mapper = new ObjectMapper();
					message += mapper.writeValueAsString(ret);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					message += "Unable to geocode: "
							+ (String) request.getAttributes().get("location");
				}

				response.setEntity(message, MediaType.APPLICATION_JSON);
			}

		};

		Restlet reverseNominatimRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					GenericEntity ret = null;

					ret = JsonTranslator.INSTANCE.translate(new DummyDataSource(
							"org-nominatim-openstreetmap",
							"http://nominatim.openstreetmap.org/reverse?format=json"
									+ (String) request.getAttributes().get(
											"coordinates"), null, null, null, null));

					ObjectMapper mapper = new ObjectMapper();
					message += mapper.writeValueAsString(ret);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					message += "Unable to reverse-geocode: "
							+ (String) request.getAttributes().get(
									"coordinates");
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
									"org-openstreetmap-nominatim",
									"nominatim",
									"OpenStreetMap Community",
									"http://www.openstreetmap.org",
									"Nominatim is a tool to search OSM data by name and address and to generate synthetic addresses of OSM points (reverse geocoding). Usage policy: http://wiki.openstreetmap.org/wiki/Nominatim_usage_policy",
									"http://nominatim.openstreetmap.org",
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

		routes.put("/services/de/nominatim/coordinates/{location}",
				nominatimRestlet);
		routes.put("/services/de/nominatim/reverse{coordinates}",
				reverseNominatimRestlet);
		routes.put("/services/de/nominatim/metadata", metadataRestlet);

		return routes;
	}

}
