/*
    Open Data Service
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
package org.jvalue.ods.grabber.pegelonline;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.GenericData;
import org.jvalue.ods.data.ListValueType;
import org.jvalue.ods.data.ValueType;
import org.jvalue.ods.data.pegelonline.Measurement;
import org.jvalue.ods.data.pegelonline.Station;
import org.jvalue.ods.data.pegelonline.Water;
import org.jvalue.ods.grabber.generic.HttpJsonReader;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class PegelOnlineGrabber.
 */
public class PegelOnlineGrabber {

	/**
	 * Gets the stations.
	 * 
	 * @return the stations
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public List<Station> getStationData() throws JsonParseException,
			JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);        
       
        Map<String, ValueType<?,?>> attributes = new HashMap<String, ValueType<?,?>>();
        fillAttributesRec(rootNode, attributes);
        GenericData genData = new GenericData(attributes);
        if (genData != null)
        {
        	
        }
        
        
       
                
        List<Station> stationData = mapper.readValue(json,
				new TypeReference<List<Station>>() {
				});

		return stationData;
	}

	private void fillAttributesRec(JsonNode objNode, Map<String, ValueType<?,?>> attributes) {
		if (objNode.isArray()) {
			//ToDo: Fix
			LinkedList<ValueType> list = new LinkedList<ValueType>();
			ValueType listValueType = new ListValueType(list, list.getClass().getName());
			
		    for (final JsonNode node : objNode) {
		    	Iterator<Map.Entry<String,JsonNode>> fields = node.fields();
				while (fields.hasNext()) {
				    Map.Entry<String,JsonNode> field = fields.next();	   
				    if (field.getValue().isArray())
				    {
				    	Map<String, ValueType<?,?>> attr = new HashMap<String, ValueType<?,?>>();
				    	fillAttributesRec(field.getValue(), attr);
				    	attributes.put(field.getKey(), new ValueType<Map<String, ValueType<?,?>>, String>(attr, attr.getClass().getName()));
				    }
				    else
				    	attributes.put(field.getKey(), new ValueType<String, String>(field.getValue().asText(), field.getValue().asText().getClass().getName()));
				}
		    }
		}
		
		
		
	}

	/**
	 * Gets the measurement of station.
	 *
	 * @param stationUUID the station uuid
	 * @param timeseriesShortname the timeseries shortname
	 * @param days the days
	 * @return the measurement of station
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Measurement> getMeasurementOfStation(String stationUUID,
			String timeseriesShortname, int days) throws JsonParseException,
			JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations/"
						+ stationUUID + "/" + timeseriesShortname
						+ "/measurements.json?start=P" + days + "D");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		List<Measurement> measurementData = mapper.readValue(json,
				new TypeReference<List<Measurement>>() {
				});
		return measurementData;

	}

	/**
	 * Gets the measurement data.
	 * 
	 * @return the measurement data
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public List<Water> getWaterData() throws JsonParseException,
			JsonMappingException, IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://www.pegelonline.wsv.de/webservices/rest-api/v2/waters.json?includeStations=true&includeTimeseries=true&includeCurrentMeasurement=true");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		List<Water> waterData = mapper.readValue(json,
				new TypeReference<List<Water>>() {
				});
		return waterData;
	}

}
