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
package org.jvalue.ods.grabber;

import java.io.File;

import org.jvalue.ods.data.DataSource;
import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.JsonNode;


public final class GrabberFactory {

	private GrabberFactory() { }


	public static Grabber<Document> getXmlGrabber(DataSource source) {
		return new XmlGrabber(source);
	}


	public static Grabber<JsonNode> getJsonGrabber(DataSource source) {
		return new JsonGrabber(source);
	}


	public static Grabber<File> getResourceGrabber(DataSource source) {
		return new ResourceGrabber(source);
	}


	public static Grabber<String> getHttpGrabber(DataSource source, String encoding) {
		return new HttpGrabber(source, encoding);
	}

}
