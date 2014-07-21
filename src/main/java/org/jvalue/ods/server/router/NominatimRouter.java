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
import org.jvalue.ods.server.restlet.BaseRestlet;
import org.jvalue.ods.server.utils.RestletResult;
import org.jvalue.ods.translator.TranslatorFactory;
import org.restlet.Request;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.ObjectMapper;

class NominatimRouter implements Router<Restlet> {

	private static final ObjectMapper mapper = new ObjectMapper();

	private HashMap<String, Restlet> routes;


	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		Restlet nominatimRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				GenericEntity ret = TranslatorFactory.getJsonTranslator(
						DummyDataSource.newInstance(
							"org-nominatim-openstreetmap",
							"http://nominatim.openstreetmap.org/search?q="
							+ (String) request.getAttributes().get("location") 
							+ "&format=json"))
					.translate();

				return RestletResult.newSuccessResult(mapper.valueToTree(ret));
			}

		};

		Restlet reverseNominatimRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				GenericEntity ret = TranslatorFactory.getJsonTranslator(
						DummyDataSource.newInstance(
							"org-nominatim-openstreetmap",
							"http://nominatim.openstreetmap.org/reverse?format=json"
							+ (String) request.getAttributes().get("coordinates")))
					.translate();

				return RestletResult.newSuccessResult(mapper.valueToTree(ret));
			}

		};

		Restlet metadataRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				return RestletResult.newSuccessResult(mapper.valueToTree(
							new JacksonMetaData(
								"org-openstreetmap-nominatim",
								"nominatim",
								"OpenStreetMap Community",
								"http://www.openstreetmap.org",
								"Nominatim is a tool to search OSM data by name and address and to generate synthetic addresses of OSM points (reverse geocoding). Usage policy: http://wiki.openstreetmap.org/wiki/Nominatim_usage_policy",
								"http://nominatim.openstreetmap.org",
								"http://www.openstreetmap.org/copyright")));
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
