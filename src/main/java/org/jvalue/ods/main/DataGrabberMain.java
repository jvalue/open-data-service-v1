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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.ListValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NullSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.grabber.Grabber;
import org.jvalue.ods.grabber.JsonGrabber;
import org.jvalue.ods.grabber.OsmGrabber;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class DataGrabberMain.
 */
public class DataGrabberMain {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		insertOsmFilesIntoDatabase();
		insertPegelOnlineStationsIntoDatabase();
		insertPoiIntoDatabase();
	}

	/**
	 * Insert poi into database.
	 */
	private static void insertPoiIntoDatabase() {
		DbAccessor<JsonNode> accessor = DbFactory.createDbAccessor("ods");
		accessor.connect();

		createView("_design/poi", "getPoiByStation",
				"function(doc) { if(doc.poi) emit (doc.longname, doc.poi) }",
				accessor);
		createView("_design/poi", "getPoiIdByStation",
				"function(doc) { if(doc.poi) emit (doc.longname, doc._id) }",
				accessor);

	}

	/**
	 * Insert osm files into database.
	 * 
	 */
	private static void insertOsmFilesIntoDatabase() {
		Grabber grabber = new OsmGrabber();

		Schema schema = createOsmSchema();
		ListValue data = (ListValue) grabber.grab(new DataSource(
				"/nbgcity.osm", schema));

		if (data == null) {
			return;
		}

		DbAccessor<JsonNode> accessor = DbFactory.createDbAccessor("ods");
		accessor.connect();
		accessor.deleteDatabase();

		accessor.insert(new JacksonMetaData(
				"org-openstreetmap",
				"openstreetmap",
				"OpenStreetMap Community",
				"http://www.openstreetmap.org",
				"OpenStreetMap ist eine Karte der Welt, erstellt von Menschen wie dir und frei verwendbar unter einer offenen Lizenz.",
				"http://www.openstreetmap.org",
				"http://www.openstreetmap.org/copyright"));

		List<MapValue> list = new LinkedList<MapValue>();

		for (GenericValue gv : data.getList()) {
			list.add((MapValue) gv);
		}

		accessor.executeBulk(list, (MapSchema) schema);

		createView(
				"_design/osm",
				"getNodeById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.nodeId) emit( doc.nodeId, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getWayById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.wayId) emit( doc.wayId, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getRelationById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.relationId) emit( doc.relationId, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getOsmDataById",
				"function(doc) { if (doc.dataType == 'Osm' && doc.nodeId) {emit(doc.nodeId, doc)} else if (doc.wayId) { emit(doc.wayId, doc)} else if (doc.relationId) { emit(doc.relationId, doc)}}",
				accessor);

		createView(
				"_design/osm",
				"getDocumentsByKeyword",
				"function(doc) { if(doc.dataType == 'Osm' && doc.tags){ for (var i in doc.tags) { emit(doc.tags[i], doc) }} }",
				accessor);

		createView(
				"_design/osm",
				"getMetadata",
				"function(doc) { if(doc.title == 'openstreetmap') emit(null, doc)}",
				accessor);

		createView(
				"_design/osm",
				"getCouchIdByOsmId",
				"function(doc) { if (doc.dataType == 'Osm' && doc.nodeId) {emit(doc.nodeId, doc._id)} else if (doc.wayId) { emit(doc.wayId, doc._id)} else if (doc.relationId) { emit(doc.relationId, doc._id)}}",
				accessor);
		createView("_design/osm", "getClassObject",
				"function(doc) { if(doc.rest_name.osm) emit (null, doc) }",
				accessor);

		createView("_design/osm", "getClassObjectId",
				"function(doc) { if(doc.rest_name.osm) emit (null, doc._id) }",
				accessor);
	}

	private static Schema createOsmSchema() {
		Map<String, Schema> nodeTagsMap = new HashMap<String, Schema>();
		MapSchema nodeTagsSchema = new MapSchema(nodeTagsMap);

		Map<String, Schema> nodeMap = new HashMap<String, Schema>();
		nodeMap.put("type", new StringSchema());
		nodeMap.put("nodeId", new StringSchema());
		nodeMap.put("timestamp", new StringSchema());
		nodeMap.put("uid", new NumberSchema());
		nodeMap.put("user", new StringSchema());
		nodeMap.put("changeset", new NumberSchema());
		nodeMap.put("latitude", new NumberSchema());
		nodeMap.put("longitude", new NumberSchema());
		nodeMap.put("tags", nodeTagsSchema);
		MapSchema nodeMapSchema = new MapSchema(nodeMap);

		// two class object strings, must not be "required"
		Map<String, Schema> type = new HashMap<String, Schema>();
		type.put("Osm", new NullSchema());
		MapSchema typeSchema = new MapSchema(type);
		nodeMap.put("objectType", typeSchema);
		Map<String, Schema> restName = new HashMap<String, Schema>();
		restName.put("osm", new NullSchema());
		MapSchema restNameSchema = new MapSchema(restName);
		nodeMap.put("rest_name", restNameSchema);

		// Map<String, Schema> wayTagsMap = new HashMap<String, Schema>();
		// MapSchema wayTagsSchema = new MapSchema(wayTagsMap);
		//
		// List<Schema> wayNodesList = new LinkedList<Schema>();
		// wayNodesList.add(new NumberSchema());
		// ListSchema wayNodesSchema = new ListSchema(wayNodesList);
		//
		// Map<String, Schema> wayMap = new HashMap<String, Schema>();
		// wayMap.put("type", new StringSchema());
		// wayMap.put("wayId", new StringSchema());
		// wayMap.put("timestamp", new StringSchema());
		// wayMap.put("uid", new NumberSchema());
		// wayMap.put("user", new StringSchema());
		// wayMap.put("changeset", new NumberSchema());
		// wayMap.put("version", new NumberSchema());
		// wayMap.put("tags", wayTagsSchema);
		// wayMap.put("nd", wayNodesSchema);
		// MapSchema wayMapSchema = new MapSchema(wayMap);
		//

		// List<Schema> entityList = new LinkedList<Schema>();
		// entityList.add(nodeMapSchema);
		// // entityList.add(wayMapSchema);
		// ListSchema listSchema = new ListSchema(entityList);

		return nodeMapSchema;
	}

	/**
	 * Insert pegel online stations into database.
	 * 
	 */
	private static void insertPegelOnlineStationsIntoDatabase() {

		Grabber grabber = new JsonGrabber();

		Schema schema = createPegelOnlineSchema();
		ListSchema ls = (ListSchema) schema;
		MapSchema stationSchema = (MapSchema) ls.getList().get(0);

		ListValue list = (ListValue) grabber
				.grab(new DataSource(
						"http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true",
						schema));

		DbAccessor<JsonNode> accessor = DbFactory.createDbAccessor("ods");
		accessor.connect();

		accessor.insert(new JacksonMetaData(
				"de-pegelonline",
				"pegelonline",
				"Wasser- und Schifffahrtsverwaltung des Bundes (WSV)",
				"https://www.pegelonline.wsv.de/adminmail",
				"PEGELONLINE stellt kostenfrei tagesaktuelle Rohwerte verschiedener gewässerkundlicher Parameter (z.B. Wasserstand) der Binnen- und Küstenpegel der Wasserstraßen des Bundes bis maximal 30 Tage rückwirkend zur Ansicht und zum Download bereit.",
				"https://www.pegelonline.wsv.de",
				"http://www.pegelonline.wsv.de/gast/nutzungsbedingungen"));

		List<MapValue> listMap = new LinkedList<MapValue>();

		for (GenericValue gv : list.getList()) {
			listMap.add((MapValue) gv);
		}

		accessor.executeBulk(listMap, stationSchema);

		// ToDo: IdPaths for createDesignDocument in a map, to have unique ids?
		createView(
				"_design/pegelonline",
				"getSingleStation",
				"function(doc) { if(doc.dataType == 'Station') emit(doc.longname, doc)}",
				accessor);

		createView(
				"_design/pegelonline",
				"getMetadata",
				"function(doc) { if(doc.title == 'pegelonline') emit(null, doc)}",
				accessor);

		createView(
				"_design/pegelonline",
				"getAllStationsFlat",
				"function(doc) { if(doc.dataType == 'Station') emit (null, doc.longname) }",
				accessor);
		createView(
				"_design/pegelonline",
				"getAllStations",
				"function(doc) { if(doc.dataType == 'Station')emit (null, doc) }",
				accessor);
		createView(
				"_design/pegelonline",
				"getStationId",
				"function(doc) { if(doc.dataType == 'Station') emit (doc.longname, doc._id) }",
				accessor);

		createView(
				"_design/pegelonline",
				"getClassObject",
				"function(doc) { if(doc.rest_name.stations) emit (null, doc) }",
				accessor);

		createView(
				"_design/pegelonline",
				"getClassObjectId",
				"function(doc) { if(doc.rest_name.stations) emit (null, doc._id) }",
				accessor);
	}

	/**
	 * Creates the pegel online schema.
	 * 
	 * @return the schema
	 */
	private static Schema createPegelOnlineSchema() {
		Map<String, Schema> water = new HashMap<String, Schema>();
		water.put("shortname", new StringSchema());
		water.put("longname", new StringSchema());
		MapSchema waterSchema = new MapSchema(water);

		Map<String, Schema> currentMeasurement = new HashMap<String, Schema>();
		currentMeasurement.put("timestamp", new StringSchema());
		currentMeasurement.put("value", new NumberSchema());
		currentMeasurement.put("trend", new NumberSchema());
		currentMeasurement.put("stateMnwMhw", new StringSchema());
		currentMeasurement.put("stateNswHsw", new StringSchema());
		MapSchema currentMeasurementSchema = new MapSchema(currentMeasurement);

		Map<String, Schema> gaugeZero = new HashMap<String, Schema>();
		gaugeZero.put("unit", new StringSchema());
		gaugeZero.put("value", new NumberSchema());
		gaugeZero.put("validFrom", new StringSchema());
		MapSchema gaugeZeroSchema = new MapSchema(gaugeZero);

		Map<String, Schema> comment = new HashMap<String, Schema>();
		comment.put("shortDescription", new StringSchema());
		comment.put("longDescription", new StringSchema());
		MapSchema commentSchema = new MapSchema(comment);

		Map<String, Schema> timeSeries = new HashMap<String, Schema>();
		timeSeries.put("shortname", new StringSchema());
		timeSeries.put("longname", new StringSchema());
		timeSeries.put("unit", new StringSchema());
		timeSeries.put("equidistance", new NumberSchema());
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapSchema timeSeriesSchema = new MapSchema(timeSeries);

		List<Schema> timeSeriesList = new LinkedList<Schema>();
		timeSeriesList.add(timeSeriesSchema);
		ListSchema timeSeriesListSchema = new ListSchema(timeSeriesList);

		Map<String, Schema> station = new HashMap<String, Schema>();
		station.put("uuid", new StringSchema());
		station.put("number", new StringSchema());
		station.put("shortname", new StringSchema());
		station.put("longname", new StringSchema());
		station.put("km", new NumberSchema());
		station.put("agency", new StringSchema());
		station.put("longitude", new NumberSchema());
		station.put("latitude", new NumberSchema());
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, Schema> type = new HashMap<String, Schema>();
		type.put("Station", new NullSchema());
		MapSchema typeSchema = new MapSchema(type);
		station.put("objectType", typeSchema);
		Map<String, Schema> restName = new HashMap<String, Schema>();
		restName.put("stations", new NullSchema());
		MapSchema restNameSchema = new MapSchema(restName);
		station.put("rest_name", restNameSchema);
		MapSchema stationSchema = new MapSchema(station);

		List<Schema> stationList = new LinkedList<Schema>();
		stationList.add(stationSchema);
		ListSchema listSchema = new ListSchema(stationList);

		return listSchema;
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
