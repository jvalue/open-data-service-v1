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
import java.util.Collection;
import java.util.LinkedList;

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.data.ListValue;
import org.jvalue.ods.data.osm.OsmData;
import org.jvalue.ods.data.pegelonline.PegelOnlineMetaData;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.grabber.JsonGrabber;
import org.jvalue.ods.grabber.OsmGrabber;
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
	public static void main(String[] args) {
		insertOsmFilesIntoDatabase();
		insertPegelOnlineStationsIntoDatabase();
	}

	/**
	 * Insert osm files into database.
	 * 
	 */
	private static void insertOsmFilesIntoDatabase() {
		OsmGrabber grabber = new OsmGrabber();
		OsmData data = null;

		data = grabber.grab(osmSource);

		if (data == null) {
			return;
		}

		DbAccessor<JsonNode> accessor = DbFactory.createDbAccessor("osm");
		accessor.connect();
		accessor.deleteDatabase();

		Collection<Object> list = new LinkedList<Object>();
		list.addAll(data.getNodes());
		list.addAll(data.getWays());
		list.addAll(data.getRelations());

		accessor.executeBulk(list);

		createView("_design/osm", "getNodeById",
				"function(doc) { if(doc.nodeId) emit( doc.nodeId, doc)}",
				accessor);

		createView("_design/osm", "getWayById",
				"function(doc) { if(doc.wayId) emit( doc.wayId, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getRelationById",
				"function(doc) { if(doc.relationId) emit( doc.relationId, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getDocumentsByKeyword",
				"function(doc) { if(doc.tags){ for (var i in doc.tags) { emit(doc.tags[i], doc) }} }",
				accessor);

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
	private static void insertPegelOnlineStationsIntoDatabase() {

		JsonGrabber grabber = new JsonGrabber();

		// generic import
		ListValue list = (ListValue) grabber
				.grab("http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true",
						"src/main/resources/schema/pegelonline_schema.json");
		DbAccessor<JsonNode> accessor = DbFactory
				.createDbAccessor("pegelonline");
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

		createView("_design/pegelonline", "getPoiByStation",
				"function(doc) { if(doc.poi) emit (doc.longname, doc.poi) }",
				accessor);
		createView("_design/pegelonline", "getAllStationsFlat",
				"function(doc) { if(doc.longname) emit (null, doc.longname) }",
				accessor);

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
