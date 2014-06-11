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

import static org.jvalue.ods.data.schema.AllowedValueTypes.VALUETYPE_NULL;
import static org.jvalue.ods.data.schema.AllowedValueTypes.VALUETYPE_NUMBER;
import static org.jvalue.ods.data.schema.AllowedValueTypes.VALUETYPE_STRING;

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
import org.jvalue.ods.data.schema.GenericValueType;
import org.jvalue.ods.data.schema.ListComplexValueType;
import org.jvalue.ods.data.schema.MapComplexValueType;
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

		GenericValueType genericObjectType = getDataSourceSchema();

		ListObject list = (ListObject) translator
				.translate(new DataSource(
						"http://pegelonline.wsv.de/webservices/rest-api/v2/stations.json?includeTimeseries=true&includeCurrentMeasurement=true&includeCharacteristicValues=true",
						genericObjectType));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDataSourceSchema()
	 */
	@Override
	public GenericValueType getDataSourceSchema() {

		List<GenericValueType> stationList = new LinkedList<GenericValueType>();
		stationList.add(getDbSchema());
		ListComplexValueType listObjectType = new ListComplexValueType(stationList);

		return listObjectType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDbSchema()
	 */
	@Override
	public MapComplexValueType getDbSchema() {
		Map<String, GenericValueType> water = new HashMap<String, GenericValueType>();
		water.put("shortname", VALUETYPE_STRING);
		water.put("longname", VALUETYPE_STRING);
		MapComplexValueType waterSchema = new MapComplexValueType(water);

		Map<String, GenericValueType> currentMeasurement = new HashMap<String, GenericValueType>();
		currentMeasurement.put("timestamp", VALUETYPE_STRING);
		currentMeasurement.put("value", VALUETYPE_NUMBER);
		currentMeasurement.put("trend", VALUETYPE_NUMBER);
		currentMeasurement.put("stateMnwMhw", VALUETYPE_STRING);
		currentMeasurement.put("stateNswHsw", VALUETYPE_STRING);
		MapComplexValueType currentMeasurementSchema = new MapComplexValueType(currentMeasurement);

		Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
		gaugeZero.put("unit", VALUETYPE_STRING);
		gaugeZero.put("value", VALUETYPE_NUMBER);
		gaugeZero.put("validFrom", VALUETYPE_STRING);
		MapComplexValueType gaugeZeroSchema = new MapComplexValueType(gaugeZero);

		Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
		comment.put("shortDescription", VALUETYPE_STRING);
		comment.put("longDescription", VALUETYPE_STRING);
		MapComplexValueType commentSchema = new MapComplexValueType(comment);

		Map<String, GenericValueType> timeSeries = new HashMap<String, GenericValueType>();
		timeSeries.put("shortname", VALUETYPE_STRING);
		timeSeries.put("longname", VALUETYPE_STRING);
		timeSeries.put("unit", VALUETYPE_STRING);
		timeSeries.put("equidistance", VALUETYPE_NUMBER);
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);

		Map<String, GenericValueType> characteristicValues = new HashMap<String, GenericValueType>();
		characteristicValues.put("shortname", VALUETYPE_STRING);
		characteristicValues.put("longname", VALUETYPE_STRING);
		characteristicValues.put("unit", VALUETYPE_STRING);
		characteristicValues.put("value", VALUETYPE_NUMBER);
		characteristicValues.put("validFrom", VALUETYPE_STRING);
		characteristicValues.put("timespanStart", VALUETYPE_STRING);
		characteristicValues.put("timespanEnd", VALUETYPE_STRING);
		List<GenericValueType> occurrences = new LinkedList<GenericValueType>();
		occurrences.add(VALUETYPE_STRING);
		ListComplexValueType occurrencesSchema = new ListComplexValueType(occurrences);
		characteristicValues.put("occurrences", occurrencesSchema);
		MapComplexValueType characteristicValuesSchema = new MapComplexValueType(characteristicValues);

		List<GenericValueType> characteristicValuesList = new LinkedList<GenericValueType>();
		characteristicValuesList.add(characteristicValuesSchema);

		ListComplexValueType characteristicValuesListSchema = new ListComplexValueType(characteristicValuesList);
		timeSeries.put("characteristicValues", characteristicValuesListSchema);


		MapComplexValueType timeSeriesSchema = new MapComplexValueType(timeSeries);

		List<GenericValueType> timeSeriesList = new LinkedList<GenericValueType>();
		timeSeriesList.add(timeSeriesSchema);
		ListComplexValueType timeSeriesListSchema = new ListComplexValueType(timeSeriesList);

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("uuid", VALUETYPE_STRING);
		station.put("number", VALUETYPE_STRING);
		station.put("shortname", VALUETYPE_STRING);
		station.put("longname", VALUETYPE_STRING);
		station.put("km", VALUETYPE_NUMBER);
		station.put("agency", VALUETYPE_STRING);
		station.put("longitude", VALUETYPE_NUMBER);
		station.put("latitude", VALUETYPE_NUMBER);
		station.put("water", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
		type.put("Station", VALUETYPE_NULL);
		MapComplexValueType typeSchema = new MapComplexValueType(type);
		station.put("objectType", typeSchema);
		Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
		restName.put("stations", VALUETYPE_NULL);
		MapComplexValueType restNameSchema = new MapComplexValueType(restName);
		station.put("rest_name", restNameSchema);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

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
