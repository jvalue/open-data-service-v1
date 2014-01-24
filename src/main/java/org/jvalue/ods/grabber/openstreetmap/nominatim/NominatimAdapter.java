/*
 * 
 */
package org.jvalue.ods.grabber.openstreetmap.nominatim;

import java.io.IOException;
import java.util.List;

import org.jvalue.ods.data.opensteetmap.nominatim.NominatimQueryResult;
import org.jvalue.ods.grabber.generic.HttpJsonReader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class NominatimAdapter.
 */
public class NominatimAdapter {

	/**
	 * Gets the nominatim data.
	 *
	 * @param query the query
	 * @return the nominatim data
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<NominatimQueryResult> getNominatimData(String query) throws JsonParseException,
			JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://nominatim.openstreetmap.org/search?q="+ query + "&format=json&limit=10&countrycodes=ger&");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		List<NominatimQueryResult> stationData = mapper.readValue(json,
				new TypeReference<List<NominatimQueryResult>>() {
				});

		return stationData;
	}
	
}
