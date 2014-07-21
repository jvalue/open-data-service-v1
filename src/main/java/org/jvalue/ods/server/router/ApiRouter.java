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
import java.util.Map;
import java.util.Set;

import org.jvalue.ods.server.restlet.BaseRestlet;
import org.jvalue.ods.server.utils.RestletResult;
import org.jvalue.ods.utils.Assert;
import org.restlet.Request;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


class ApiRouter implements Router<Restlet> {

	private static final ObjectMapper mapper = new ObjectMapper();


	private final JsonNode apiJsonData;

	public ApiRouter(Set<String> routes) {
		Assert.assertNotNull(routes);
		this.apiJsonData = mapper.valueToTree(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {

		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		// creates a visualization of the API
		Restlet apiRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				return RestletResult.newSuccessResult(apiJsonData);
			}
		};

		routes.put("/api", apiRestlet);
		return routes;
	}

}
