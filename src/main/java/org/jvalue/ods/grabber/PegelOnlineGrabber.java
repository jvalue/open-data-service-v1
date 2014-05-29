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
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NullSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
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

		Schema schema = getDataSourceSchema();

		ListObject list = (ListObject) translator
				.translate(new DataSource(
						"http://www.pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true",
						schema));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDataSourceSchema()
	 */
	@Override
	public Schema getDataSourceSchema() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDbSchema()
	 */
	@Override
	public MapSchema getDbSchema() {
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
