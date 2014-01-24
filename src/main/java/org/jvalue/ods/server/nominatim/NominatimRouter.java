/*
 * 
 */
package org.jvalue.ods.server.nominatim;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.opensteetmap.nominatim.NominatimQueryResult;
import org.jvalue.ods.grabber.openstreetmap.nominatim.NominatimAdapter;
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
			public NominatimRouter() {}
				

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
			 */
			@Override
			public Map<String, Restlet> getRoutes() {
				routes = new HashMap<String, Restlet>();

				// gets data from all stations
				Restlet nominatimRestlet = new Restlet() {
					@Override
					public void handle(Request request, Response response) {
						// Print the requested URI path
						String message = "";
						try {

							List<NominatimQueryResult> sd = null;
							try {
								sd = new NominatimAdapter().getNominatimData("Rothsee");
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


				routes.put("/nominatim", nominatimRestlet);
				

				return routes;
			}


}
