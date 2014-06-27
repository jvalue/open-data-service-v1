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
package org.jvalue.ods.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jvalue.ods.data.DataSourceVisitor;
import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.sources.OsmSource;
import org.jvalue.ods.data.sources.PegelOnlineSource;
import org.jvalue.ods.data.sources.PegelPortalMvSource;
import org.jvalue.ods.data.valuetypes.AllowedValueTypes;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.ListComplexValueType;
import org.jvalue.ods.data.valuetypes.MapComplexValueType;
import org.jvalue.ods.data.valuetypes.SimpleValueType;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.translator.JsonTranslator;
import com.fasterxml.jackson.databind.JsonNode;


public final class RenameSourceVisitor implements DataSourceVisitor<Void, Void> {

	@Override
	public Void visit(PegelOnlineSource source, Void param) {
		// if (!SchemaManager.validateGenericValusFitsSchema(data, schema)) {
		// Logging.info(this.getClass(),
		// "Could not validate schema in CombineFilter.");
		// return data;
		// }
		// TODO

		MapComplexValueType sourceStructure = createSourceWaterStructure();
		MapComplexValueType destinationStructure = createDestinationWaterStructure();
		String newName = "BodyOfWater";
		MapComplexValueType combinedSchema = createRenamedSchema();

		MapObject mv = new MapObject();

		try {
			DbAccessor<JsonNode> accessor = DbFactory.createDbAccessor("ods");
			accessor.connect();

			List<JsonNode> nodes = accessor.executeDocumentQuery(
					"_design/pegelonline", "getAllStations", null);

			for (JsonNode station : nodes) {
				if (station.isObject()) {

					GenericEntity gv = JsonTranslator.INSTANCE
							.convertJson(station);

					traverseSchema(sourceStructure, newName, gv, mv.getMap());
					insertRenamedValue(gv, mv, destinationStructure);
					
					

					// if (!SchemaManager.validateGenericValusFitsSchema(mv,
					// combinedSchema)) {
					// System.err
					// .println("Validation of quality-enhanced data failed.");
					// }

					// TODO

					try {

						MapObject finalMo = (MapObject) gv;
						if (!finalMo.getMap().containsKey("dataStatus"))
						{
							finalMo.getMap().put("dataStatus",
								new BaseObject("improved"));
						}

						accessor.insert(gv);

					} catch (Exception ex) {
						String errmsg = "Could not insert MapValue: "
								+ ex.getMessage();
						System.err.println(errmsg);
						Logging.error(this.getClass(), errmsg);
						throw new RuntimeException(errmsg);
					}

				}
			}
			accessor.insert(combinedSchema);

		} catch (DbException e) {

			String errmsg = "Error during Quality Assurance. " + e.getMessage();
			System.err.println(errmsg);
			Logging.error(this.getClass(), errmsg);
			throw new RuntimeException(errmsg);

		}

		return null;
	}


	@Override
	public Void visit(PegelPortalMvSource source, Void param) {
		return null;
	}

	@Override
	public Void visit(OsmSource source, Void param) {
		return null;
	}



	private void traverseSchema(GenericValueType sourceStructure, String newName, Serializable serializable, Map<String, Serializable> map) {

		if (sourceStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) sourceStructure)
					.getMap().entrySet()) {

				if (e.getValue() instanceof SimpleValueType) {
					map.put(newName, ((MapObject) serializable).getMap()
							.remove(e.getKey()));

				} else {
					if (e.getValue() instanceof MapComplexValueType)
					{
						MapComplexValueType mcvt = (MapComplexValueType) e.getValue();
						if (mcvt.getMap() != null)
						{
							traverseSchema(e.getValue(), newName, ((MapObject) serializable)
									.getMap().get(e.getKey()), map);
						}
						else
						{
							map.put(newName, ((MapObject) serializable).getMap()
									.remove(e.getKey()));
						}
					}					
				}
			}
		} else if (sourceStructure instanceof ListComplexValueType) {
			for (Serializable gv : ((ListObject) serializable).getList()) {
				traverseSchema(((ListComplexValueType) sourceStructure)
						.getList().get(0), newName, gv, map);
			}
		}
	}

	/**
	 * Insert renamed value.
	 * 
	 * @param serializable
	 *            the serializable
	 * @param mv
	 *            the mv
	 * @param destinationStructure
	 *            the destination structure
	 */
	private void insertRenamedValue(Serializable serializable, MapObject mv,
			GenericValueType destinationStructure) {

		if (destinationStructure instanceof MapComplexValueType) {

			for (Entry<String, GenericValueType> e : ((MapComplexValueType) destinationStructure)
					.getMap().entrySet()) {

				if (e.getValue() == null) {

					if (serializable instanceof MapObject) {			
						Serializable ser = mv.getMap().get(e.getKey());
						
						// check for correct value here
						((MapObject) serializable).getMap().put(e.getKey(), ser);
					} else {
						String errmsg = "Invalid renamedSchema.";
						Logging.error(this.getClass(), errmsg);
						System.err.println(errmsg);
						throw new RuntimeException(errmsg);
					}

				} else {

					if (((MapObject) serializable).getMap().get(e.getKey()) == null) {
						((MapObject) serializable).getMap().put(e.getKey(),
								new MapObject());
					}

					insertRenamedValue(((MapObject) serializable).getMap()
							.get(e.getKey()), mv, e.getValue());
				}
			}
		} else if (destinationStructure instanceof ListComplexValueType) {

			if (!(serializable instanceof ListObject)) {
				String errmsg = "Invalid renameSchema.";
				Logging.error(this.getClass(), errmsg);
				System.err.println(errmsg);
				throw new RuntimeException(errmsg);
			}

			for (Serializable gv : ((ListObject) serializable).getList()) {

				insertRenamedValue(gv, mv,
						((ListComplexValueType) destinationStructure).getList()
								.get(0));
			}

		}

	}


	/**
	 * Creates the renamed schema.
	 * 
	 * @return the map schema
	 */
	private static MapComplexValueType createRenamedSchema() {
		Map<String, GenericValueType> water = new HashMap<String, GenericValueType>();
		water.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		water.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType waterSchema = new MapComplexValueType(water);

		Map<String, GenericValueType> currentMeasurement = new HashMap<String, GenericValueType>();
		currentMeasurement.put("timestamp", AllowedValueTypes.VALUETYPE_STRING);
		currentMeasurement.put("value", AllowedValueTypes.VALUETYPE_NUMBER);
		currentMeasurement.put("trend", AllowedValueTypes.VALUETYPE_NUMBER);
		currentMeasurement.put("stateMnwMhw",
				AllowedValueTypes.VALUETYPE_STRING);
		currentMeasurement.put("stateNswHsw",
				AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType currentMeasurementSchema = new MapComplexValueType(
				currentMeasurement);

		Map<String, GenericValueType> gaugeZero = new HashMap<String, GenericValueType>();
		gaugeZero.put("unit", AllowedValueTypes.VALUETYPE_STRING);
		gaugeZero.put("value", AllowedValueTypes.VALUETYPE_NUMBER);
		gaugeZero.put("validFrom", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType gaugeZeroSchema = new MapComplexValueType(gaugeZero);

		Map<String, GenericValueType> comment = new HashMap<String, GenericValueType>();
		comment.put("shortDescription", AllowedValueTypes.VALUETYPE_STRING);
		comment.put("longDescription", AllowedValueTypes.VALUETYPE_STRING);
		MapComplexValueType commentSchema = new MapComplexValueType(comment);

		Map<String, GenericValueType> timeSeries = new HashMap<String, GenericValueType>();
		timeSeries.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		timeSeries.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		timeSeries.put("unit", AllowedValueTypes.VALUETYPE_STRING);
		timeSeries.put("equidistance", AllowedValueTypes.VALUETYPE_NUMBER);
		timeSeries.put("currentMeasurement", currentMeasurementSchema);
		timeSeries.put("gaugeZero", gaugeZeroSchema);
		timeSeries.put("comment", commentSchema);
		MapComplexValueType timeSeriesSchema = new MapComplexValueType(
				timeSeries);

		List<GenericValueType> timeSeriesList = new LinkedList<GenericValueType>();
		timeSeriesList.add(timeSeriesSchema);
		ListComplexValueType timeSeriesListSchema = new ListComplexValueType(
				timeSeriesList);

		Map<String, GenericValueType> coordinate = new HashMap<>();
		coordinate.put("longitude", AllowedValueTypes.VALUETYPE_NUMBER);
		coordinate.put("latitude", AllowedValueTypes.VALUETYPE_NUMBER);
		MapComplexValueType coordinateSchema = new MapComplexValueType(
				coordinate);

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("coordinate", coordinateSchema);
		station.put("uuid", AllowedValueTypes.VALUETYPE_STRING);
		station.put("number", AllowedValueTypes.VALUETYPE_STRING);
		station.put("shortname", AllowedValueTypes.VALUETYPE_STRING);
		station.put("longname", AllowedValueTypes.VALUETYPE_STRING);
		station.put("km", AllowedValueTypes.VALUETYPE_NUMBER);
		station.put("agency", AllowedValueTypes.VALUETYPE_STRING);
		station.put("BodyOfWater", waterSchema);
		station.put("timeseries", timeSeriesListSchema);
		// two class object strings, must not be "required"
		Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
		type.put("Station", AllowedValueTypes.VALUETYPE_NULL);
		MapComplexValueType typeSchema = new MapComplexValueType(type);
		station.put("objectType", typeSchema);
		Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
		restName.put("stations", AllowedValueTypes.VALUETYPE_NULL);
		MapComplexValueType restNameSchema = new MapComplexValueType(restName);
		station.put("restName", restNameSchema);
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}

	/**
	 * Creates the coordinate schema.
	 * 
	 * @return the map schema
	 */
	private static MapComplexValueType createSourceWaterStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();

		station.put("water", new MapComplexValueType(null));
		MapComplexValueType stationSchema = new MapComplexValueType(station);

		return stationSchema;
	}

	/**
	 * Creates the destination coordinate structure.
	 * 
	 * @return the map schema
	 */
	private static MapComplexValueType createDestinationWaterStructure() {

		Map<String, GenericValueType> station = new HashMap<String, GenericValueType>();
		station.put("BodyOfWater", null);
		MapComplexValueType stationSchema = new MapComplexValueType(
				station);

		return stationSchema;
	}

}
