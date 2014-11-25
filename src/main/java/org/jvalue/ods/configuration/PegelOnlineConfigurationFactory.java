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
package org.jvalue.ods.configuration;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceConfiguration;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.objecttypes.ListObjectType;
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.OdsView;
import org.jvalue.ods.db.SourceDataRepository;
import org.jvalue.ods.filter.FilterChainElement;
import org.jvalue.ods.filter.FilterFactory;
import org.jvalue.ods.filter.adapter.SourceAdapterFactory;
import org.jvalue.ods.utils.JsonPropertyKey;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NULL;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NUMBER;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_STRING;

public final class PegelOnlineConfigurationFactory {

	private final DataSourceConfiguration configuration;

	@Inject
	public PegelOnlineConfigurationFactory(
			DbFactory dbFactory,
			FilterFactory filterFactory,
			SourceAdapterFactory sourceAdapterFactory) {

		DataSource dataSource = createDataSource();
		SourceDataRepository dataRepository = dbFactory.createSourceDataRepository("pegelonline", dataSource.getDomainIdKey());
		FilterChainElement<Void, ?> filterChain = createFilterChain(dataSource, dataRepository, sourceAdapterFactory, filterFactory);
		this.configuration = new DataSourceConfiguration(dataSource, filterChain, dataRepository);

	}


	public DataSourceConfiguration createConfiguration() {
		return configuration;
	}


	private DataSource createDataSource() {
		String sourceId = "de-pegelonline";
		String url = "http://pegelonline.wsv.de/webservices/rest-api/v2/"
				+ "stations.json?includeTimeseries=true"
				+ "&includeCurrentMeasurement=true"
				+ "&includeCharacteristicValues=true"
				+ "&waters=ELBE";

		ListObjectType sourceSchema;
		MapObjectType improvedDbSchema;
		MapObjectType rawDbSchema;

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

		// improved db schema
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
					"de-pegelonline-currentMeasurement", currentMeasurement,
					null);

			Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
			gaugeZero.put("unit", VALUETYPE_STRING);
			gaugeZero.put("value", VALUETYPE_NUMBER);
			gaugeZero.put("validFrom", VALUETYPE_STRING);

			MapObjectType gaugeZeroGot = new MapObjectType(
					"de-pegelonline-gaugeZero", gaugeZero, null);

			Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
			comment.put("shortDescription", VALUETYPE_STRING);
			comment.put("longDescription", VALUETYPE_STRING);

			MapObjectType commentGot = new MapObjectType(
					"de-pegelonline-comment", comment, null);

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
			characteristicValuesAttributes.put("timespanStart",
					VALUETYPE_STRING);
			characteristicValuesAttributes.put("timespanEnd", VALUETYPE_STRING);

			Map<String, ObjectType> characteristicValuesReferencedObjects = new HashMap<String, ObjectType>();

			List<GenericValueType> occurrences = new LinkedList<GenericValueType>();
			occurrences.add(VALUETYPE_STRING);

			ListObjectType occurrencesType = new ListObjectType(occurrences,
					null);

			characteristicValuesReferencedObjects.put("occurrences",
					occurrencesType);

			MapObjectType characteristicValuesType = new MapObjectType(
					"de-pegelonline-characteristicValues",
					characteristicValuesAttributes,
					characteristicValuesReferencedObjects);

			List<ObjectType> characteristicValuesTypesList = new LinkedList<>();
			characteristicValuesTypesList.add(characteristicValuesType);

			ListObjectType characteristicValuesGot = new ListObjectType(null,
					characteristicValuesTypesList);

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

			Map<String, GenericValueType> coordinateValues = new HashMap<String, GenericValueType>();
			coordinateValues.put("longitude", VALUETYPE_NUMBER);
			coordinateValues.put("latitude", VALUETYPE_NUMBER);

			MapObjectType coordinateGot = new MapObjectType(
					"de-pegelonline-coordinate", coordinateValues, null);

			Map<String, ObjectType> stationReferencedObjects = new HashMap<String, ObjectType>();

			stationReferencedObjects.put("BodyOfWater", waterGot);
			stationReferencedObjects.put("timeseries", timeSeriesGot);
			stationReferencedObjects.put("coordinate", coordinateGot);

			// two class object strings, must not be "required"
			Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
			type.put("Station", VALUETYPE_NULL);

			ObjectType typeType = new MapObjectType("objectType", type, null);

			stationReferencedObjects.put("objectType", typeType);

			Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
			restName.put("stations", VALUETYPE_NULL);

			ObjectType restNameType = new MapObjectType("restName", restName,
					null);

			stationReferencedObjects.put("restName", restNameType);

			MapObjectType stationType = new MapObjectType(
					"de-pegelonline-station", stationAttributes,
					stationReferencedObjects);

			stationType.setDataQualityStatus("improved");

			improvedDbSchema = stationType;
		}

		// raw db schema
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
					"de-pegelonline-currentMeasurement", currentMeasurement,
					null);

			Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
			gaugeZero.put("unit", VALUETYPE_STRING);
			gaugeZero.put("value", VALUETYPE_NUMBER);
			gaugeZero.put("validFrom", VALUETYPE_STRING);

			MapObjectType gaugeZeroGot = new MapObjectType(
					"de-pegelonline-gaugeZero", gaugeZero, null);

			Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
			comment.put("shortDescription", VALUETYPE_STRING);
			comment.put("longDescription", VALUETYPE_STRING);

			MapObjectType commentGot = new MapObjectType(
					"de-pegelonline-comment", comment, null);

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
			characteristicValuesAttributes.put("timespanStart",
					VALUETYPE_STRING);
			characteristicValuesAttributes.put("timespanEnd", VALUETYPE_STRING);

			Map<String, ObjectType> characteristicValuesReferencedObjects = new HashMap<String, ObjectType>();

			List<GenericValueType> occurrences = new LinkedList<GenericValueType>();
			occurrences.add(VALUETYPE_STRING);

			ListObjectType occurrencesType = new ListObjectType(occurrences,
					null);

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

			ObjectType restNameType = new MapObjectType("restName", restName,
					null);

			stationReferencedObjects.put("restName", restNameType);

			MapObjectType stationType = new MapObjectType(
					"de-pegelonline-station", stationAttributes,
					stationReferencedObjects);

			stationType.setDataQualityStatus("raw");

			rawDbSchema = stationType;
		}

		// source schema
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
					"de-pegelonline-currentMeasurement", currentMeasurement,
					null);

			Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
			gaugeZero.put("unit", VALUETYPE_STRING);
			gaugeZero.put("value", VALUETYPE_NUMBER);
			gaugeZero.put("validFrom", VALUETYPE_STRING);

			MapObjectType gaugeZeroGot = new MapObjectType(
					"de-pegelonline-gaugeZero", gaugeZero, null);

			Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
			comment.put("shortDescription", VALUETYPE_STRING);
			comment.put("longDescription", VALUETYPE_STRING);

			MapObjectType commentGot = new MapObjectType(
					"de-pegelonline-comment", comment, null);

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
			characteristicValuesAttributes.put("timespanStart",
					VALUETYPE_STRING);
			characteristicValuesAttributes.put("timespanEnd", VALUETYPE_STRING);

			Map<String, ObjectType> characteristicValuesReferencedObjects = new HashMap<String, ObjectType>();

			List<GenericValueType> occurrences = new LinkedList<GenericValueType>();
			occurrences.add(VALUETYPE_STRING);

			ListObjectType occurrencesType = new ListObjectType(occurrences,
					null);

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

			ObjectType restNameType = new MapObjectType("restName", restName,
					null);

			stationReferencedObjects.put("restName", restNameType);

			MapObjectType stationType = new MapObjectType(
					"de-pegelonline-station", stationAttributes,
					stationReferencedObjects);

			List<ObjectType> stationList = new LinkedList<ObjectType>();
			stationList.add(stationType);
			sourceSchema = new ListObjectType(null, stationList);

		}

		// ods views
		/*
		{
			// improved data quality

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getSingleStation",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'improved') emit(doc.longname, doc)}"));

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getAllStationsFlat",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'improved') emit (null, doc.longname) }"));
			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getAllStations",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'improved')emit (null, doc) }"));
			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getStationId",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'improved') emit (doc.longname, doc._id) }"));

			// raw

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getSingleStationRaw",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'raw') emit(doc.longname, doc)}"));

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getAllStationsFlatRaw",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'raw') emit (null, doc.longname) }"));
			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getAllStationsRaw",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'raw')emit (null, doc) }"));
			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getStationIdRaw",
					"function(doc) { if(doc.dataType == 'Station' && doc.dataQualityStatus == 'raw') emit (doc.longname, doc._id) }"));

			odsViews.add(new OdsView("_design/pegelonline", "getMetadata",
					"function(doc) { if(doc.title == 'pegelonline') emit(null, doc)}"));

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getClassObject",
					"function(doc) { if(doc.name == 'de-pegelonline-station' && doc.dataQualityStatus == 'improved') emit (null, doc) }"));

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getClassObjectId",
					"function(doc) { if(doc.name == 'de-pegelonline-station' && doc.dataQualityStatus == 'improved') emit (null, doc._id) }"));

			// raw class objects
			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getClassObjectRaw",
					"function(doc) { if(doc.name == 'de-pegelonline-station' && doc.dataQualityStatus == 'raw') emit (null, doc) }"));

			odsViews.add(new OdsView(
					"_design/pegelonline",
					"getClassObjectIdRaw",
					"function(doc) { if(doc.name == 'de-pegelonline-station' && doc.dataQualityStatus == 'raw') emit (null, doc._id) }"));
		}
					*/

		return new DataSource(
				sourceId,
				url,
				sourceSchema,
				rawDbSchema,
				improvedDbSchema,
				metaData,
				new LinkedList<OdsView>(), new JsonPropertyKey.Builder().stringPath("uuid").build());
	}


	private FilterChainElement<Void, ?> createFilterChain(
			DataSource dataSource,
			SourceDataRepository dataRepository,
			SourceAdapterFactory sourceAdapterFactory,
			FilterFactory filterFactory) {
		FilterChainElement<Void, ArrayNode> chain = FilterChainElement.instance(
				sourceAdapterFactory.createJsonSourceAdapter(dataSource));

		chain
				.setNextFilter(filterFactory.createDbInsertionFilter(dataSource, dataRepository))
				.setNextFilter(filterFactory.createNotificationFilter(dataSource));
				// .setNextFilter(new DataAdditionFilter(source))
				// .setNextFilter(
						// new CombineSourceFilter(
								// createSourceCoordinateStructure(),
								// createDestinationCoordinateStructure()))
				// .setNextFilter(
						// new RenameSourceFilter(createSourceWaterStructure(),
								// createDestinationWaterStructure(),
								// "BodyOfWater"))
				// .setNextFilter(new DbInsertionFilter(accessor, source))
				// .setNextFilter(new PegelOnlineQualityAssurance())

		return chain;
	}


	/*
	private static MapComplexValueType createSourceCoordinateStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();

		station.put("longitude", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("latitude", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}

	private static MapComplexValueType createDestinationCoordinateStructure() {

		Map<String, GenericValueType> coordinate = new HashMap<String, GenericValueType>();

		coordinate.put("coordinate", null);
		MapComplexValueType coordinateSchema = new MapComplexValueType(
				coordinate);

		return coordinateSchema;
	}

	private static MapComplexValueType createSourceWaterStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();

		station.put("water", new MapComplexValueType(null));
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}

	private static MapComplexValueType createDestinationWaterStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("BodyOfWater", null);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}
	*/
}
