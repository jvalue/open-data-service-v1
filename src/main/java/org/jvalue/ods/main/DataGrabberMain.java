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

import java.io.IOException;

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.data.ListValue;
import org.jvalue.ods.data.MapValue;
import org.jvalue.ods.data.StringValue;
import org.jvalue.ods.data.pegelonline.PegelOnlineMetaData;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.grabber.JsonGrabber;
import org.jvalue.ods.grabber.XmlGrabber;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class DataGrabberMain.
 */
public class DataGrabberMain {

	/** The Constant osmSource. */
	private final static String osmSource = "nbgcity.osm";

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws JsonParseException,
			JsonMappingException, IOException {
		insertOsmFilesIntoDatabase();
		insertPegelOnlineStationsIntoDatabase();
	}

	/**
	 * Insert osm files into database.
	 * 
	 */
	private static void insertOsmFilesIntoDatabase() {
		XmlGrabber xmlGrabber = new XmlGrabber();
		GenericValue gv = xmlGrabber.grab(osmSource);
		MapValue mv = (MapValue) gv;
		MapValue osm = (MapValue) mv.getMap().get("osm");

		DbAccessor<JsonNode> accessor = DbFactory.createCouchDbAccessor("osm");
		accessor.connect();
		accessor.deleteDatabase();

		insertNodeType(accessor, osm, "node");
		insertNodeType(accessor, osm, "way");
		insertNodeType(accessor, osm, "relation");

		createView("_design/osm", "getNodeById",
				"function(doc) { if(doc.type == 'node') emit( doc.id, doc)}",
				accessor);

		createView("_design/osm", "getWayById",
				"function(doc) { if(doc.type == 'way') emit( doc.id, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getRelationById",
				"function(doc) { if(doc.type == 'relation') emit( doc.id, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getDocumentsByKeyword",
				"function(doc) { if(doc.tag){ for (var i in doc.tag) {var tmp = doc.tag[i]; for (var j in tmp) {emit(tmp[j], doc)} } }}",
				accessor);

	}

	/**
	 * Insert node type.
	 * 
	 * @param accessor
	 *            the accessor
	 * @param osm
	 *            the osm
	 * @param type
	 *            the type
	 */
	private static void insertNodeType(DbAccessor<JsonNode> accessor,
			MapValue osm, String type) {

		ListValue list = (ListValue) osm.getMap().get(type);

		for (GenericValue node : list.getList()) {
			MapValue map = (MapValue) node;
			map.getMap().put("type", new StringValue(type));
			accessor.insert(map);
		}

	}

	/**
	 * Insert pegel online stations into database.
	 * 
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void insertPegelOnlineStationsIntoDatabase()
			throws JsonParseException, JsonMappingException, IOException {

		JsonGrabber grabber = new JsonGrabber();

		// generic import
		ListValue list = (ListValue) grabber
				.grab("http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true");
		DbAccessor<JsonNode> accessor = DbFactory
				.createCouchDbAccessor("pegelonline");
		accessor.connect();
		accessor.deleteDatabase();
		accessor.insert(new PegelOnlineMetaData());
		for (GenericValue gv : list.getList()) {
			accessor.insert(gv);
		}

		// ToDo: IdPaths for createDesignDocument in a map, to have unique ids?
		createView("_design/pegelonline", "getSingleStation",
				"function(doc) { if(doc.longname) emit( doc.longname, doc)}",
				accessor);
		createView(
				"_design/pegelonline",
				"getMeasurements",
				"function(doc) { if(doc.longname) emit( doc.longname, doc.timeseries)}",
				accessor);
		createView("_design/pegelonline", "getMetadata",
				"function(doc) { if(doc.title) emit(null, doc)}", accessor);
	}

	/**
	 * Creates the pegel online design document.
	 * 
	 * @param idPath
	 *            the id path
	 * @param viewName
	 *            the view name
	 * @param function
	 *            the function
	 * @param accessor
	 *            the accessor
	 */
	private static void createView(String idPath, String viewName,
			String function, DbAccessor<JsonNode> accessor) {

		DesignDocument dd = null;
		boolean update = true;

		try {
			dd = accessor.getDocument(DesignDocument.class, idPath);
		} catch (DbException e) {
			DesignDocumentFactory fac = new StdDesignDocumentFactory();
			dd = fac.newDesignDocumentInstance();
			dd.setId(idPath);
			update = false;
		}

		View v = new DesignDocument.View();
		v.setMap(function);
		dd.addView(viewName, v);

		try {
			if (update) {
				accessor.update(dd);
			} else {
				accessor.insert(dd);
			}
		} catch (UpdateConflictException ex) {
			System.err.println("Design Document already exists."
					+ ex.getMessage());
			Logging.error(DataGrabberMain.class,
					"Design Document already exists." + ex.getMessage());
		}

	}
}
