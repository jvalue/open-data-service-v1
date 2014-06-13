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
package org.jvalue.ods.data.sources;

import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NULL;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NUMBER;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_STRING;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.objecttypes.ListObjectType;
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.data.valuetypes.GenericValueType;

public final class PegelOnlineSource extends DataSource {

	public static DataSource createInstance() {

		String sourceId = "de-pegelonline";
		String url = "http://pegelonline.wsv.de/webservices/rest-api/v2/"
				+ "stations.json?includeTimeseries=true"
				+ "&includeCurrentMeasurement=true"
				+ "&includeCharacteristicValues=true";

		ListObjectType sourceSchema;
		MapObjectType dbSchema;

		OdsMetaData metaData = new JacksonMetaData(
				sourceId,
				"pegelonline",
				"Wasser- und Schifffahrtsverwaltung des Bundes (WSV)",
				"https://www.pegelonline.wsv.de/adminmail",
				"PEGELONLINE stellt kostenfrei tagesaktuelle Rohwerte verschiedener"
						+ " gewässerkundlicher Parameter (z.B. Wasserstand) der Binnen- und Küstenpegel"
						+ " der Wasserstraßen des Bundes bis maximal 30 Tage rückwirkend zur Ansicht und"
						+ " zum Download bereit.",
				"https://www.pegelonline.wsv.de",
				"http://www.pegelonline.wsv.de/gast/nutzungsbedingungen");

		List<OdsView> odsViews = new LinkedList<OdsView>();

		// db schema
		{
			Map<String, GenericValueType> water = new HashMap<String, GenericValueType>();
			water.put("shortname", VALUETYPE_STRING);
			water.put("longname", VALUETYPE_STRING);

			MapObjectType waterGot = new MapObjectType("de-pegelonline-water",
					water, null);

			Map<String, GenericValueType> currentMeasurement = new HashMap<String, GenericValueType>();
			currentMeasurement.put("timestamp", VALUETYPE_STRING);
			currentMeasurement.put("value", VALUETYPE_NUMBER);
			currentMeasurement.put("trend", VALUETYPE_NUMBER);
			currentMeasurement.put("stateMnwMhw", VALUETYPE_STRING);
			currentMeasurement.put("stateNswHsw", VALUETYPE_STRING);
			MapObjectType currentMeasurementGot = new MapObjectType(
					"de-pegelonline-currentMeasurement", currentMeasurement, null);

			Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
			gaugeZero.put("unit", VALUETYPE_STRING);
			gaugeZero.put("value", VALUETYPE_NUMBER);
			gaugeZero.put("validFrom", VALUETYPE_STRING);

			MapObjectType gaugeZeroGot = new MapObjectType(
					"de-pegelonline-gaugeZero", gaugeZero, null);

			Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
			comment.put("shortDescription", VALUETYPE_STRING);
			comment.put("longDescription", VALUETYPE_STRING);

			MapObjectType commentGot = new MapObjectType("de-pegelonline-comment",
					comment, null);

			Map<String, GenericValueType> timeSeriesAttributes = new HashMap<String, GenericValueType>();
			timeSeriesAttributes.put("shortname", VALUETYPE_STRING);
			timeSeriesAttributes.put("longname", VALUETYPE_STRING);
			timeSeriesAttributes.put("unit", VALUETYPE_STRING);
			timeSeriesAttributes.put("equidistance", VALUETYPE_NUMBER);

			Map<String, ObjectType> timeSeriesReferencedObjects = new HashMap<String, ObjectType>();

			timeSeriesReferencedObjects.put("currentMeasurement",
					currentMeasurementGot);
			timeSeriesReferencedObjects.put("gaugeZero", gaugeZeroGot);
			timeSeriesReferencedObjects.put("comment", commentGot);

			Map<String, GenericValueType> characteristicValuesAttributes = new HashMap<String, GenericValueType>();
			characteristicValuesAttributes.put("shortname", VALUETYPE_STRING);
			characteristicValuesAttributes.put("longname", VALUETYPE_STRING);
			characteristicValuesAttributes.put("unit", VALUETYPE_STRING);
			characteristicValuesAttributes.put("value", VALUETYPE_NUMBER);
			characteristicValuesAttributes.put("validFrom", VALUETYPE_STRING);
			characteristicValuesAttributes.put("timespanStart", VALUETYPE_STRING);
			characteristicValuesAttributes.put("timespanEnd", VALUETYPE_STRING);

			Map<String, ObjectType> characteristicValuesReferencedObjects = new HashMap<String, ObjectType>();

			List<GenericValueType> occurrences = new LinkedList<GenericValueType>();
			occurrences.add(VALUETYPE_STRING);

			ListObjectType occurrencesType = new ListObjectType(occurrences, null);

			characteristicValuesReferencedObjects.put("occurrences",
					occurrencesType);

			MapObjectType characteristicValuesGot = new MapObjectType(
					"de-pegelonline-characteristicValues",
					characteristicValuesAttributes,
					characteristicValuesReferencedObjects);

			timeSeriesReferencedObjects.put("characteristicValues",
					characteristicValuesGot);

			MapObjectType timeSeriesType = new MapObjectType(
					"de-pegelonline-timeSeries", timeSeriesAttributes,
					timeSeriesReferencedObjects);

			List<ObjectType> timeseriesTypesList = new LinkedList<>();
			timeseriesTypesList.add(timeSeriesType);

			ListObjectType timeSeriesGot = new ListObjectType(null,
					timeseriesTypesList);

			Map<String, GenericValueType> stationAttributes = new HashMap<String, GenericValueType>();
			stationAttributes.put("uuid", VALUETYPE_STRING);
			stationAttributes.put("number", VALUETYPE_STRING);
			stationAttributes.put("shortname", VALUETYPE_STRING);
			stationAttributes.put("longname", VALUETYPE_STRING);
			stationAttributes.put("km", VALUETYPE_NUMBER);
			stationAttributes.put("agency", VALUETYPE_STRING);
			stationAttributes.put("longitude", VALUETYPE_NUMBER);
			stationAttributes.put("latitude", VALUETYPE_NUMBER);

			Map<String, ObjectType> stationReferencedObjects = new HashMap<String, ObjectType>();

			stationReferencedObjects.put("water", waterGot);
			stationReferencedObjects.put("timeseries", timeSeriesGot);

			// two class object strings, must not be "required"
			Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
			type.put("Station", VALUETYPE_NULL);

			ObjectType typeType = new MapObjectType("objectType", type, null);

			stationReferencedObjects.put("objectType", typeType);

			Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
			restName.put("stations", VALUETYPE_NULL);

			ObjectType restNameType = new MapObjectType("rest_name", restName, null);

			stationReferencedObjects.put("rest_name", restNameType);

			
			
			
			
			MapObjectType stationType = new MapObjectType("de-pegelonline-station",
					stationAttributes, stationReferencedObjects);

			dbSchema = stationType;
		}

		// source schema
		{
			List<ObjectType> stationList = new LinkedList<ObjectType>();
			stationList.add(dbSchema);
			sourceSchema = new ListObjectType(null, stationList);

		}

		// ods views
		{
			odsViews.add(new OdsView("_design/pegelonline", "getSingleStation",
					"function(doc) { if(doc.dataType == 'Station') emit(doc.longname, doc)}"));

			odsViews.add(new OdsView("_design/pegelonline", "getMetadata",
					"function(doc) { if(doc.title == 'pegelonline') emit(null, doc)}"));

			odsViews.add(new OdsView("_design/pegelonline", "getAllStationsFlat",
					"function(doc) { if(doc.dataType == 'Station') emit (null, doc.longname) }"));
			odsViews.add(new OdsView("_design/pegelonline", "getAllStations",
					"function(doc) { if(doc.dataType == 'Station')emit (null, doc) }"));
			odsViews.add(new OdsView("_design/pegelonline", "getStationId",
					"function(doc) { if(doc.dataType == 'Station') emit (doc.longname, doc._id) }"));

			odsViews.add(new OdsView("_design/pegelonline", "getClassObject",
					"function(doc) { if(doc.name == 'de-pegelonline-station') emit (null, doc) }"));

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getClassObjectId",
					"function(doc) { if(doc.name == 'de-pegelonline-station') emit (null, doc._id) }"));

		}
		return new PegelOnlineSource(sourceId, url, sourceSchema, dbSchema, metaData, odsViews);
	}


	protected PegelOnlineSource(
			String id, 
			String url, 
			ObjectType sourceSchema,
			ObjectType dbSchema,
			OdsMetaData metaData,
			List<OdsView> odsViews) {
		
		super(id, url, sourceSchema, dbSchema, metaData, odsViews);
	}

}
