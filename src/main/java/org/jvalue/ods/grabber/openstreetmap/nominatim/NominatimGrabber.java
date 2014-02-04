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
package org.jvalue.ods.grabber.openstreetmap.nominatim;

import java.io.IOException;
import java.util.List;

import org.jvalue.ods.data.opensteetmap.nominatim.NominatimQueryResult;
import org.jvalue.ods.data.opensteetmap.nominatim.NominatimReverseQueryResult;
import org.jvalue.ods.grabber.HttpJsonReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class NominatimGrabber.
 */
public class NominatimGrabber {

	/**
	 * Gets the nominatim data.
	 * 
	 * @param query
	 *            the query
	 * @return the nominatim data
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public List<NominatimQueryResult> getNominatimData(String query)
			throws JsonParseException, JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://nominatim.openstreetmap.org/search?q=" + query
						+ "&format=json&limit=10&countrycodes=ger&");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		List<NominatimQueryResult> res = mapper.readValue(json,
				new TypeReference<List<NominatimQueryResult>>() {
				});

		return res;
	}

	/**
	 * Gets the reverse nominatim data.
	 * 
	 * @param query
	 *            the query
	 * @return the reverse nominatim data
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public NominatimReverseQueryResult getReverseNominatimData(String query)
			throws JsonParseException, JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://nominatim.openstreetmap.org/reverse?format=json"
						+ query);
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		NominatimReverseQueryResult ret = mapper.readValue(json,
				new TypeReference<NominatimReverseQueryResult>() {
				});

		return ret;
	}

}
