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
package org.jvalue.ods.grabber.openstreetmap.overpass;

import java.io.IOException;

import org.jvalue.ods.data.openstreetmap.overpass.Overpass;
import org.jvalue.ods.grabber.generic.HttpJsonReader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class OverpassGrabber.
 */
public class OverpassGrabber {

	/**
	 * Gets the location data.
	 * 
	 * @param string
	 *            the string
	 * @return the location data
	 * @throws IOException
	 */
	public Overpass getLocationData(String query) throws IOException {
		HttpJsonReader httpAdapter = new HttpJsonReader(
				"http://overpass.osm.rambler.ru/cgi/interpreter?data=[out:json];node[name%3D"
						+ query + "]%3Bout%3B");
		String json = httpAdapter.getJSON("UTF-8");

		ObjectMapper mapper = new ObjectMapper();
		Overpass res = mapper.readValue(json, new TypeReference<Overpass>() {
		});

		return res;
	}

}
