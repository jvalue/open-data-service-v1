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
package org.jvalue.ods.adapter.pegelonline;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.adapter.RouterInterface;
import org.jvalue.ods.adapter.pegelonline.data.Station;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PegelOnlineRouter.
 *
 */
public class PegelOnlineRouter implements RouterInterface {


	/* (non-Javadoc)
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		Restlet singleStationRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<Station> sd = new PegelOnlineAdapter()
							.getStationData();

					for (Station s : sd) {
						if (s.getLongname()
								.equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))
								|| s.getShortname().equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))) {

							ObjectMapper mapper = new ObjectMapper();
							message += mapper.writeValueAsString(s);

							break;
						}
					}

					response.setEntity(message, MediaType.APPLICATION_JSON);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Restlet stationsRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<Station> sd = new PegelOnlineAdapter()
							.getStationData();

					for (Station s : sd) {

						ObjectMapper mapper = new ObjectMapper();
						message += mapper.writeValueAsString(s);

					}

					response.setEntity(message, MediaType.APPLICATION_JSON);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Restlet currentMeasurementRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "";
				try {

					List<Station> sd = new PegelOnlineAdapter()
							.getStationData();

					for (Station s : sd) {
						if (s.getLongname()
								.equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))
								|| s.getShortname().equalsIgnoreCase(
										(String) request.getAttributes().get(
												"station"))) {

							ObjectMapper mapper = new ObjectMapper();
							message += mapper.writeValueAsString(s
									.getTimeseries().get(0)
									.getCurrentMeasurement());
							break;
						}
					}

					response.setEntity(message, MediaType.APPLICATION_JSON);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		routes.put("/pegelonline/stations", stationsRestlet);
		routes.put("/pegelonline/stations/{station}", singleStationRestlet);
		routes.put("/pegelonline/stations/{station}/currentMeasurement",
				currentMeasurementRestlet);

		return routes;
	}

}
