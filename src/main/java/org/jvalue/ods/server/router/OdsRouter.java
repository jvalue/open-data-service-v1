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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.server.restlet.AccessObjectByIdRestlet;
import org.jvalue.ods.server.restlet.BaseRestlet;
import org.jvalue.ods.server.utils.RestletResult;
import org.jvalue.ods.utils.Assert;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class OdsRouter implements Router<Restlet> {

	private static final ObjectMapper mapper = new ObjectMapper();


	private final DbAccessor<JsonNode> dbAccessor;

	public OdsRouter(DbAccessor<JsonNode> dbAccessor) {
		Assert.assertNotNull(dbAccessor);
		this.dbAccessor = dbAccessor;
	}


	@Override
	public Map<String, Restlet> getRoutes() {

		HashMap<String, Restlet> routes = new HashMap<String, Restlet>();

		Restlet odsRestlet = new BaseRestlet(
				new HashSet<String>(),
				true) {

			@Override
			protected RestletResult doGet(Request request) {
				// there is an attribute in url
				if (request.getResourceRef().getQueryAsForm().size() != 1)
					return onBadRequest("no argument given");

				JsonNode data = RouterUtils.getDocumentByAttribute(dbAccessor, request);
				return RestletResult.newSuccessResult(data);
			}
		};

		Restlet odsSchemaRestlet = new BaseRestlet(
				new HashSet<String>(),
				true) {

			@Override
			protected RestletResult doGet(Request request) {
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
							return RestletResult.newSuccessResult(nodes.get(0));
						} else {
							return RestletResult.newErrorResult(
									Status.CLIENT_ERROR_NOT_FOUND,
									"No schema found for name '" + value + "'");
						}

					} else {

						JsonNode data = RouterUtils.getDocumentByAttribute(dbAccessor, request);
						return RestletResult.newSuccessResult(data);

					}


				} else {

					dbAccessor.connect();
					List<JsonNode> nodes = dbAccessor.executeDocumentQuery(
							"_design/ods", "getAllClassObjects", null);

					JsonNode resultData = mapper.valueToTree(nodes);
					return RestletResult.newSuccessResult(resultData);
				}

			}
		};

		routes.put("/ods/${id}", new AccessObjectByIdRestlet(dbAccessor));
		routes.put("/ods", odsRestlet);
		routes.put("/ods/schema", odsSchemaRestlet);

		return routes;
	}

}
