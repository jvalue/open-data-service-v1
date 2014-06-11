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
import org.jvalue.ods.translator.OsmTranslator;

/**
 * The Class OsmGrabber.
 */
public class OsmGrabber implements Grabber {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#grab()
	 */
	@Override
	public GenericEntity grab() {

		Translator translator = new OsmTranslator();

		GenericValueType genericObjectType = getDataSourceSchema();

		ListObject list = (ListObject) translator.translate(new DataSource(
				"/nbgcity.osm", genericObjectType));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDataSourceSchema()
	 */
	@Override
	public GenericValueType getDataSourceSchema() {
		Map<String, GenericValueType> nodeTagsMap = new HashMap<String, GenericValueType>();
		MapComplexValueType nodeTagsSchema = new MapComplexValueType(nodeTagsMap);

		Map<String, GenericValueType> nodeMap = new HashMap<String, GenericValueType>();
		nodeMap.put("type", VALUETYPE_STRING);
		nodeMap.put("nodeId", VALUETYPE_STRING);
		nodeMap.put("timestamp", VALUETYPE_STRING);
		nodeMap.put("uid", VALUETYPE_NUMBER);
		nodeMap.put("user", VALUETYPE_STRING);
		nodeMap.put("changeset", VALUETYPE_NUMBER);
		nodeMap.put("latitude", VALUETYPE_NUMBER);
		nodeMap.put("longitude", VALUETYPE_NUMBER);
		nodeMap.put("tags", nodeTagsSchema);
		MapComplexValueType nodeMapSchema = new MapComplexValueType(nodeMap);

		// two class object strings, must not be "required"
		Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
		type.put("Osm", VALUETYPE_NULL);
		MapComplexValueType typeSchema = new MapComplexValueType(type);
		nodeMap.put("objectType", typeSchema);
		Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
		restName.put("osm", VALUETYPE_NULL);
		MapComplexValueType restNameSchema = new MapComplexValueType(restName);
		nodeMap.put("rest_name", restNameSchema);

		List<GenericValueType> entityList = new LinkedList<GenericValueType>();
		entityList.add(nodeMapSchema);
		ListComplexValueType listObjectType = new ListComplexValueType(entityList);

		return listObjectType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDbSchema()
	 */
	@Override
	public MapComplexValueType getDbSchema() {
		Map<String, GenericValueType> nodeMap = new HashMap<String, GenericValueType>();
		nodeMap.put("type", VALUETYPE_STRING);
		nodeMap.put("nodeId", VALUETYPE_STRING);
		nodeMap.put("timestamp", VALUETYPE_STRING);
		nodeMap.put("uid", VALUETYPE_NUMBER);
		nodeMap.put("user", VALUETYPE_STRING);
		nodeMap.put("changeset", VALUETYPE_NUMBER);
		nodeMap.put("latitude", VALUETYPE_NUMBER);
		nodeMap.put("longitude", VALUETYPE_NUMBER);
		Map<String, GenericValueType> nodeTagsMap = new HashMap<String, GenericValueType>();
		MapComplexValueType nodeTagsSchema = new MapComplexValueType(nodeTagsMap);
		nodeMap.put("tags", nodeTagsSchema);

		// two class object strings, must not be "required"
		Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
		type.put("Osm", VALUETYPE_NULL);
		MapComplexValueType typeSchema = new MapComplexValueType(type);
		nodeMap.put("objectType", typeSchema);
		Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
		restName.put("osm", VALUETYPE_NULL);
		MapComplexValueType restNameSchema = new MapComplexValueType(restName);
		nodeMap.put("rest_name", restNameSchema);
		MapComplexValueType nodeMapSchema = new MapComplexValueType(nodeMap);

		return nodeMapSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#getMetaData()
	 */
	@Override
	public OdsMetaData getMetaData() {
		return new JacksonMetaData(
				"org-openstreetmap",
				"openstreetmap",
				"OpenStreetMap Community",
				"http://www.openstreetmap.org",
				"OpenStreetMap ist eine Karte der Welt, erstellt von Menschen wie dir und frei verwendbar unter einer offenen Lizenz.",
				"http://www.openstreetmap.org",
				"http://www.openstreetmap.org/copyright");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#getOdsViews()
	 */
	@Override
	public List<OdsView> getOdsViews() {
		List<OdsView> list = new LinkedList<>();

		list.add(new OdsView(
				"_design/osm",
				"getNodeById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.nodeId) emit( doc.nodeId, doc)}"));

		list.add(new OdsView("_design/osm", "getWayById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.wayId) emit( doc.wayId, doc)}"));

		list.add(new OdsView(
				"_design/osm",
				"getRelationById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.relationId) emit( doc.relationId, doc)}"));

		list.add(new OdsView(
				"_design/osm",
				"getOsmDataById",
				"function(doc) { if (doc.dataType == 'Osm' && doc.nodeId) {emit(doc.nodeId, doc)} else if (doc.wayId) { emit(doc.wayId, doc)} else if (doc.relationId) { emit(doc.relationId, doc)}}"));

		list.add(new OdsView(
				"_design/osm",
				"getDocumentsByKeyword",
				"function(doc) { if(doc.dataType == 'Osm' && doc.tags){ for (var i in doc.tags) { emit(doc.tags[i], doc) }} }"));

		list.add(new OdsView("_design/osm", "getMetadata",
				"function(doc) { if(doc.title == 'openstreetmap') emit(null, doc)}"));

		list.add(new OdsView(
				"_design/osm",
				"getCouchIdByOsmId",
				"function(doc) { if (doc.dataType == 'Osm' && doc.nodeId) {emit(doc.nodeId, doc._id)} else if (doc.wayId) { emit(doc.wayId, doc._id)} else if (doc.relationId) { emit(doc.relationId, doc._id)}}"));
		list.add(new OdsView("_design/osm", "getClassObject",
				"function(doc) { if(doc.rest_name.osm) emit (null, doc) }"));

		list.add(new OdsView("_design/osm", "getClassObjectId",
				"function(doc) { if(doc.rest_name.osm) emit (null, doc._id) }"));

		return list;
	}
}
