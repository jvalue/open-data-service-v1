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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.server.restlet.BaseRestlet;
import org.jvalue.ods.server.utils.RestletResult;
import org.jvalue.ods.translator.TranslatorFactory;
import org.jvalue.ods.utils.Assert;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


class RoutesRouter implements Router<Restlet> {
		
	
	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String
		PARAM_START = "start",
		PARAM_END = "end";


	private final DbAccessor<JsonNode> dbAccessor;

	public RoutesRouter(DbAccessor<JsonNode> dbAccessor) {
		Assert.assertNotNull(dbAccessor);
		this.dbAccessor = dbAccessor;
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		final Restlet routeRestlet = new BaseRestlet(
				new HashSet<String>(Arrays.asList(PARAM_START, PARAM_END)),
				false) {

			@Override
			protected RestletResult doGet(Request request) {
				dbAccessor.connect();

				String startStation = getParameter(request, PARAM_START).toUpperCase();
				String endStation = getParameter(request, PARAM_END).toUpperCase();

				List<JsonNode> startNodes = dbAccessor.executeDocumentQuery(
						"_design/pegelonline",
						"getSingleStation", 
						startStation);

				List<JsonNode> endNodes = dbAccessor.executeDocumentQuery(
						"_design/pegelonline",
						"getSingleStation", 
						endStation);

				if (startNodes.isEmpty() || endNodes.isEmpty()) {
					return RestletResult.newErrorResult(
							Status.CLIENT_ERROR_NOT_FOUND, 
							"no route found between '" + startStation + "' and '" + endStation + "'");
				}

				double startLongitude = 0;
				double startLatitude = 0;
				double endLongitude = 0;
				double endLatitude = 0;

				if (startNodes.get(0).isObject()) {
					JsonNode node = startNodes.get(0);
					startLatitude = node.get("coordinate").get("latitude").asDouble();
					startLongitude = node.get("coordinate").get("longitude").asDouble();
				}

				if (endNodes.get(0).isObject()) {
					JsonNode node = endNodes.get(0);
					endLatitude = node.get("coordinate").get("latitude").asDouble();
					endLongitude = node.get("coordinate").get("longitude").asDouble();
				}

				String source = "http://www.yournavigation.org/api/1.0/gosmore.php?format=kml&flat="
						+ startLatitude
						+ "&flon="
						+ startLongitude
						+ "&tlat="
						+ endLatitude
						+ "&tlon="
						+ endLongitude
						+ "&v=motorcar&fast=1&layer=mapnik";

				DataSource ds = DummyDataSource.newInstance("org-yournavigation", source);

				GenericEntity gv = TranslatorFactory.getXmlTranslator(ds).translate();
				return RestletResult.newSuccessResult(mapper.valueToTree(gv));
			}
		};

		/*
		Restlet routeDistanceRestlet = new BaseRestlet() {
			// @SuppressWarnings("unchecked")
			@Override
			protected RestletResult doGet(Request request) {

				// TODO come on, seriously?
				Response r = routeRestlet.handle(request);

				try {

					if (r.getEntity().getMediaType() == MediaType.TEXT_PLAIN) {
						throw new IOException(r.getEntityAsText());
					}

					HashMap<String, Object> route = mapper.readValue(
							r.getEntityAsText(),
							new TypeReference<HashMap<String, Object>>() {
							});

					HashMap<String, Object> kml = (HashMap<String, Object>) route
							.get("kml");
					HashMap<String, Object> document = (HashMap<String, Object>) kml
							.get("Document");
					String distance = (String) document.get("distance");

					response.setEntity(distance, MediaType.TEXT_PLAIN);

				} catch (IOException e) {
					String errorMessage = "Error during client request: " + e;
					Logging.error(this.getClass(), errorMessage);
					System.err.println(errorMessage);
					response.setEntity(r.getEntity());
				}

			}
		};
		*/

		routes.put("/services/org/jvalue/routes/route", routeRestlet);
		// routes.put("/services/org/jvalue/routes/routeDistance",
				// routeDistanceRestlet);

		return routes;
	}

}
