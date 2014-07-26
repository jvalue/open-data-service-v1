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

import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.grabber.GrabberFactory;
import org.jvalue.ods.server.restlet.BaseRestlet;
import org.jvalue.ods.server.utils.RestletResult;
import org.jvalue.ods.translator.TranslatorFactory;
import org.restlet.Request;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


class OverpassRouter implements Router<Restlet> {

	private static final ObjectMapper mapper = new ObjectMapper();


	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		Restlet overpassLocationRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				String location = request.getAttributes().get("location").toString();
				JsonNode jsonNode = GrabberFactory.getJsonGrabber(
						DummyDataSource.newInstance( 
							"ru-rambler-osm-overpass",
							"http://overpass.osm.rambler.ru/cgi/interpreter?data=[out:json];node[name%3D"
							+ location + "]%3Bout%3B")).filter(null);
				GenericEntity ret = TranslatorFactory.getJsonTranslator().translate(jsonNode);
				return RestletResult.newSuccessResult(mapper.valueToTree(ret));
			}

		};

		Restlet metadataRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				JsonNode data = mapper.valueToTree(new JacksonMetaData(
							"org-openstreetmap-overpass",
							"overpass",
							"OpenStreetMap Community",
							"http://www.openstreetmap.org",
							"The Overpass API is a read-only API that serves up custom selected parts of the OSM map data. It acts as a database over the web: the client sends a query to the API and gets back the data set that corresponds to the query.",
							"http://overpass.osm.rambler.ru",
							"http://www.openstreetmap.org/copyright"));

				return RestletResult.newSuccessResult(data);
			}
		};

		routes.put("/services/de/overpass/{location}", overpassLocationRestlet);
		routes.put("/services/de/overpass/metadata", metadataRestlet);

		return routes;
	}
}
