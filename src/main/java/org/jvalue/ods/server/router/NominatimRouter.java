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

class NominatimRouter implements Router<Restlet> {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String PARAM_LATITUDE = "latitude",
			PARAM_LONGITUDE = "longitude";

	@Override
	public Map<String, Restlet> getRoutes() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		Restlet nominatimRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				JsonNode jsonNode = GrabberFactory.getJsonGrabber(
						DummyDataSource.newInstance(
							"org-nominatim-openstreetmap",
							"http://nominatim.openstreetmap.org/search?q="
									+ (String) request.getAttributes().get(
											"location") + "&format=json"))
					.filter(null);
				GenericEntity ret = TranslatorFactory.getJsonTranslator().translate(jsonNode);

				return RestletResult.newSuccessResult(mapper.valueToTree(ret));
			}

		};

		Restlet reverseNominatimRestlet = new BaseRestlet(new HashSet<String>(
				Arrays.asList(PARAM_LATITUDE, PARAM_LONGITUDE)), false) {
			@Override
			protected RestletResult doGet(Request request) {

				String latitude = getParameter(request, PARAM_LATITUDE);
				String longitude = getParameter(request, PARAM_LONGITUDE);

				JsonNode jsonNode = GrabberFactory.getJsonGrabber(
						DummyDataSource.newInstance(
								"org-nominatim-openstreetmap",
								"http://nominatim.openstreetmap.org/reverse?format=json&lat="
										+ latitude + "&lon=" + longitude))
					.filter(null);
				GenericEntity ret = TranslatorFactory.getJsonTranslator().translate(jsonNode);

				return RestletResult.newSuccessResult(mapper.valueToTree(ret));
			}

		};

		Restlet metadataRestlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				return RestletResult
						.newSuccessResult(mapper
								.valueToTree(new JacksonMetaData(
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
		routes.put("/services/de/nominatim/reverse", reverseNominatimRestlet);
		routes.put("/services/de/nominatim/metadata", metadataRestlet);

		return routes;
	}

}
