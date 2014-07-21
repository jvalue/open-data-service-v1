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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.server.restlet.BaseRestlet;
import org.jvalue.ods.server.restlet.ExecuteQueryRestlet;
import org.jvalue.ods.server.utils.RestletResult;
import org.jvalue.ods.utils.Assert;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


class OsmRouter implements Router<Restlet> {

	private static final ObjectMapper mapper = new ObjectMapper();


	private final DbAccessor<JsonNode> dbAccessor;

	public OsmRouter(DbAccessor<JsonNode> dbAccessor) {
		Assert.assertNotNull(dbAccessor);
		this.dbAccessor = dbAccessor;
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		Restlet metadataRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				dbAccessor.connect();

				try {
					List<JsonNode> nodes = dbAccessor.executeDocumentQuery(
							"_design/osm",
							"getMetadata", 
							null);

					return RestletResult.newSuccessResult(nodes.get(0));
				} catch (DbException ex) {
					return RestletResult.newErrorResult(
							Status.CLIENT_ERROR_NOT_FOUND, 
							"Metadata not found");
				}
			}
		};

		Restlet osmRestlet = new BaseRestlet(new HashSet<String>(), true) {
			@Override
			protected RestletResult doGet(Request request) {
				// there is an attribute in url
				if (request.getResourceRef().getQueryAsForm().size() == 1) {
					try {
						String jsonString = RouterUtils.getDocumentByAttribute(request, dbAccessor);
						JsonNode data = mapper.readTree(jsonString);
						return RestletResult.newSuccessResult(data);
					} catch (IOException ioe) {
						throw new RuntimeException(ioe);
					}
				} else {
					return onBadRequest("Please specify an argument");
				}
			}
		};

		routes.put("/ods/de/osm/nodes/{osm_id}", new FetchOsmObjectRestlet("getNodeById"));
		routes.put("/ods/de/osm/ways/{osm_id}", new FetchOsmObjectRestlet("getWayById"));
		routes.put("/ods/de/osm/relations/{osm_id}", new FetchOsmObjectRestlet("getRelationById"));
		routes.put("/ods/de/osm/data/{osm_id}", new FetchOsmObjectRestlet("getOsmDataById"));
		routes.put("/ods/de/osm/keyword/{osm_keyword}", new FetchOsmObjectRestlet("getDocumentsByKeyword"));
		routes.put("/ods/de/osm/metadata", metadataRestlet);
		routes.put("/ods/de/osm", osmRestlet);
		routes.put("/ods/de/osm/$class", new ExecuteQueryRestlet.Builder(
					dbAccessor, 
					"_design/osm", 
					"getClassObject")
				.fetchAllDbEntries(false)
				.build());
		routes.put("/ods/de/osm/$class_id", new ExecuteQueryRestlet.Builder(
					dbAccessor, 
					"_design/osm", 
					"getClassObjectId")
				.fetchAllDbEntries(false)
				.build());

		return routes;
	}


	private class FetchOsmObjectRestlet extends BaseRestlet {

		private final String viewName;

		public FetchOsmObjectRestlet(String viewName) {
			Assert.assertNotNull(viewName);
			this.viewName = viewName;
		}

		@Override
		protected RestletResult doGet(Request request) {
			String osmId = (String) request.getAttributes().get("osm_id");
			try {
				dbAccessor.connect();
				List<JsonNode> nodes = dbAccessor.executeDocumentQuery(
						"_design/osm", 
						viewName,
						osmId);

				return RestletResult.newSuccessResult(mapper.valueToTree(nodes));

			} catch (DbException e) {
				return RestletResult.newErrorResult(
						Status.CLIENT_ERROR_NOT_FOUND,
						"no item with id '" + osmId + "' found");
			}
		}

	}

}
