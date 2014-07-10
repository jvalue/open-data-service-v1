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
import java.util.List;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.server.restlet.AccessObjectByIdRestlet;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class OdsRouter implements Router<Restlet> {

	private HashMap<String, Restlet> routes;

	private DbAccessor<JsonNode> dbAccessor;


	public OdsRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");
	}


	@Override
	public Map<String, Restlet> getRoutes() {

		routes = new HashMap<String, Restlet>();

		Restlet odsRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				String message = "Please specify an argument.";

				// there is an attribute in url
				if (request.getResourceRef().getQueryAsForm().size() == 1) {

					message = RouterUtils.getDocumentByAttribute(request,
							dbAccessor);

				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}
		};

		Restlet odsSchemaRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				String message = "Please specify an argument.";

				// there is an attribute in url
				if (request.getResourceRef().getQueryAsForm().size() == 1) {

					Form f = request.getResourceRef().getQueryAsForm();
					String key = f.get(0).getName();
					String value = f.get(0).getValue();

					if (key.equals("name")) {
						String[] parts = value.split("/");

						String dbKey = parts[parts.length - 1];

						dbAccessor.connect();

						List<JsonNode> nodes = dbAccessor.executeDocumentQuery(
								"_design/ods", "getClassObjectByType", dbKey);

						if (nodes != null && !nodes.isEmpty()) {
							try {
								message = new ObjectMapper()
										.writeValueAsString(nodes.get(0));
							} catch (JsonProcessingException e) {
								String errorMessage = "Error during client request: "
										+ e;
								Logging.error(this.getClass(), errorMessage);
								System.err.println(errorMessage);
							}
						}

					}

					message = RouterUtils.getDocumentByAttribute(request,
							dbAccessor);

				} else {

					dbAccessor.connect();

					List<JsonNode> nodes = dbAccessor.executeDocumentQuery(
							"_design/ods", "getAllClassObjects", null);

					if (nodes != null && !nodes.isEmpty()) {
						try {
							message = new ObjectMapper()
									.writeValueAsString(nodes);
						} catch (JsonProcessingException e) {
							String errorMessage = "Error during client request: "
									+ e;
							Logging.error(this.getClass(), errorMessage);
							System.err.println(errorMessage);
						}
					}

				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}
		};

		routes.put("/ods/${id}", new AccessObjectByIdRestlet(dbAccessor));
		routes.put("/ods", odsRestlet);
		routes.put("/ods/schema", odsSchemaRestlet);

		return routes;
	}

}
