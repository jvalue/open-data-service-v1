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
package org.jvalue.ods.main;

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.filter.CombineFilter;
import org.jvalue.ods.grabber.Translator;
import org.jvalue.ods.translator.JsonTranslator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class QualityAssuranceMain.
 */
public class QualityAssuranceMain {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws JsonProcessingException
	 *             the json processing exception
	 */
	public static void main(String[] args) throws JsonProcessingException {

		Translator jt = new JsonTranslator();

		GenericValue gv = jt
				.translate(new DataSource(
						"http://faui2o2f.cs.fau.de:8080/open-data-service/ods/de/pegelonline/stations/rethem",
						null));

		MapSchema s = createCoordinateSchema();

		MapValue mv = new CombineFilter((MapValue) gv, s, "coordinate")
				.filter();

		System.out.println(new ObjectMapper().writeValueAsString(mv));

	}

	/**
	 * Creates the coordinate schema.
	 * 
	 * @return the map schema
	 */
	private static MapSchema createCoordinateSchema() {

		Map<String, Schema> station = new HashMap<String, Schema>();

		station.put("longitude", new NumberSchema());
		station.put("latitude", new NumberSchema());
		MapSchema stationSchema = new MapSchema(station);

		return stationSchema;
	}

}
