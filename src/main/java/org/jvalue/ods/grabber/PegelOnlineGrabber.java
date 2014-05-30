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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.schema.AllowedBaseObjectTypes;
import org.jvalue.ods.data.schema.ListObjectType;
import org.jvalue.ods.data.schema.MapObjectType;
import org.jvalue.ods.data.schema.GenericObjectType;
import org.jvalue.ods.main.Grabber;
import org.jvalue.ods.translator.JsonTranslator;

/**
 * The Class PegelOnlineGrabber.
 */
public class PegelOnlineGrabber implements Grabber {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#grab()
	 */
	@Override
	public GenericEntity grab() {

		Translator translator = new JsonTranslator();

		GenericObjectType genericObjectType = getDataSourceSchema();

		ListObject list = (ListObject) translator
				.translate(new DataSource(
						"http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true",
						genericObjectType));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDataSourceSchema()
	 */
	@Override
	public GenericObjectType getDataSourceSchema() {
		Map<String, GenericObjectType> water = new HashMap<String, GenericObjectType>();
		water.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		water.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType waterSchema = new MapObjectType(water);

		Map<String, GenericObjectType> currentMeasurement = new HashMap<String, GenericObjectType>();
		currentMeasurement.put("timestamp", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		currentMeasurement.put("value", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		currentMeasurement.put("trend", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		currentMeasurement.put("stateMnwMhw", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		currentMeasurement.put("stateNswHsw", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType currentMeasurementSchema = new MapObjectType(currentMeasurement);

		Map<String, GenericObjectType> gaugeZero = new HashMap<String, GenericObjectType>();
		gaugeZero.put("unit", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		gaugeZero.put("value", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		gaugeZero.put("validFrom", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType gaugeZeroSchema = new MapObjectType(gaugeZero);

		Map<String, GenericObjectType> comment = new HashMap<String, GenericObjectType>();
		comment.put("shortDescription", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		comment.put("longDescription", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType commentSchema = new MapObjectType(comment);

		Map<String, GenericObjectType> timeSeries = new HashMap<String, GenericObjectType>();
		timeSeries.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("unit", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("equidistance", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapObjectType timeSeriesSchema = new MapObjectType(timeSeries);

		List<GenericObjectType> timeSeriesList = new LinkedList<GenericObjectType>();
		timeSeriesList.add(timeSeriesSchema);
		ListObjectType timeSeriesListSchema = new ListObjectType(timeSeriesList);

		Map<String, GenericObjectType> station = new HashMap<String, GenericObjectType>();
		station.put("uuid", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("number", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("km", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("agency", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("longitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("latitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, GenericObjectType> type = new HashMap<String, GenericObjectType>();
		type.put("Station", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType typeSchema = new MapObjectType(type);
		station.put("objectType", typeSchema);
		Map<String, GenericObjectType> restName = new HashMap<String, GenericObjectType>();
		restName.put("stations", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType restNameSchema = new MapObjectType(restName);
		station.put("rest_name", restNameSchema);
		MapObjectType stationSchema = new MapObjectType(station);

		List<GenericObjectType> stationList = new LinkedList<GenericObjectType>();
		stationList.add(stationSchema);
		ListObjectType listObjectType = new ListObjectType(stationList);

		return listObjectType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDbSchema()
	 */
	@Override
	public MapObjectType getDbSchema() {
		Map<String, GenericObjectType> water = new HashMap<String, GenericObjectType>();
		water.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		water.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType waterSchema = new MapObjectType(water);

		Map<String, GenericObjectType> currentMeasurement = new HashMap<String, GenericObjectType>();
		currentMeasurement.put("timestamp", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		currentMeasurement.put("value", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		currentMeasurement.put("trend", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		currentMeasurement.put("stateMnwMhw", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		currentMeasurement.put("stateNswHsw", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType currentMeasurementSchema = new MapObjectType(currentMeasurement);

		Map<String, GenericObjectType> gaugeZero = new HashMap<String, GenericObjectType>();
		gaugeZero.put("unit", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		gaugeZero.put("value", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		gaugeZero.put("validFrom", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType gaugeZeroSchema = new MapObjectType(gaugeZero);

		Map<String, GenericObjectType> comment = new HashMap<String, GenericObjectType>();
		comment.put("shortDescription", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		comment.put("longDescription", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		MapObjectType commentSchema = new MapObjectType(comment);

		Map<String, GenericObjectType> timeSeries = new HashMap<String, GenericObjectType>();
		timeSeries.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("unit", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		timeSeries.put("equidistance", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapObjectType timeSeriesSchema = new MapObjectType(timeSeries);

		List<GenericObjectType> timeSeriesList = new LinkedList<GenericObjectType>();
		timeSeriesList.add(timeSeriesSchema);
		ListObjectType timeSeriesListSchema = new ListObjectType(timeSeriesList);

		Map<String, GenericObjectType> station = new HashMap<String, GenericObjectType>();
		station.put("uuid", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("number", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("shortname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("longname", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("km", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("agency", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		station.put("longitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("latitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, GenericObjectType> type = new HashMap<String, GenericObjectType>();
		type.put("Station", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType typeSchema = new MapObjectType(type);
		station.put("objectType", typeSchema);
		Map<String, GenericObjectType> restName = new HashMap<String, GenericObjectType>();
		restName.put("stations", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType restNameSchema = new MapObjectType(restName);
		station.put("rest_name", restNameSchema);
		MapObjectType stationSchema = new MapObjectType(station);

		return stationSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#getMetaData()
	 */
	@Override
	public OdsMetaData getMetaData() {
		return new JacksonMetaData(
				"de-pegelonline",
				"pegelonline",
				"Wasser- und Schifffahrtsverwaltung des Bundes (WSV)",
				"https://www.pegelonline.wsv.de/adminmail",
				"PEGELONLINE stellt kostenfrei tagesaktuelle Rohwerte verschiedener gewässerkundlicher Parameter (z.B. Wasserstand) der Binnen- und Küstenpegel der Wasserstraßen des Bundes bis maximal 30 Tage rückwirkend zur Ansicht und zum Download bereit.",
				"https://www.pegelonline.wsv.de",
				"http://www.pegelonline.wsv.de/gast/nutzungsbedingungen");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#getOdsViews()
	 */
	@Override
	public List<OdsView> getOdsViews() {
		List<OdsView> list = new LinkedList<>();

		list.add(new OdsView("_design/pegelonline", "getSingleStation",
				"function(doc) { if(doc.dataType == 'Station') emit(doc.longname, doc)}"));

		list.add(new OdsView("_design/pegelonline", "getMetadata",
				"function(doc) { if(doc.title == 'pegelonline') emit(null, doc)}"));

		list.add(new OdsView("_design/pegelonline", "getAllStationsFlat",
				"function(doc) { if(doc.dataType == 'Station') emit (null, doc.longname) }"));
		list.add(new OdsView("_design/pegelonline", "getAllStations",
				"function(doc) { if(doc.dataType == 'Station')emit (null, doc) }"));
		list.add(new OdsView("_design/pegelonline", "getStationId",
				"function(doc) { if(doc.dataType == 'Station') emit (doc.longname, doc._id) }"));

		list.add(new OdsView("_design/pegelonline", "getClassObject",
				"function(doc) { if(doc.rest_name.stations) emit (null, doc) }"));

		list.add(new OdsView("_design/pegelonline", "getClassObjectId",
				"function(doc) { if(doc.rest_name.stations) emit (null, doc._id) }"));

		return list;
	}
}
