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

		GenericObjectType genericObjectType = getDataSourceSchema();

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
	public GenericObjectType getDataSourceSchema() {
		Map<String, GenericObjectType> nodeTagsMap = new HashMap<String, GenericObjectType>();
		MapObjectType nodeTagsSchema = new MapObjectType(nodeTagsMap);

		Map<String, GenericObjectType> nodeMap = new HashMap<String, GenericObjectType>();
		nodeMap.put("type", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("nodeId", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("timestamp", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("uid", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		nodeMap.put("user", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("changeset", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		nodeMap.put("latitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		nodeMap.put("longitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		nodeMap.put("tags", nodeTagsSchema);
		MapObjectType nodeMapSchema = new MapObjectType(nodeMap);

		// two class object strings, must not be "required"
		Map<String, GenericObjectType> type = new HashMap<String, GenericObjectType>();
		type.put("Osm", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType typeSchema = new MapObjectType(type);
		nodeMap.put("objectType", typeSchema);
		Map<String, GenericObjectType> restName = new HashMap<String, GenericObjectType>();
		restName.put("osm", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType restNameSchema = new MapObjectType(restName);
		nodeMap.put("rest_name", restNameSchema);

		List<GenericObjectType> entityList = new LinkedList<GenericObjectType>();
		entityList.add(nodeMapSchema);
		ListObjectType listObjectType = new ListObjectType(entityList);

		return listObjectType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDbSchema()
	 */
	@Override
	public MapObjectType getDbSchema() {
		Map<String, GenericObjectType> nodeMap = new HashMap<String, GenericObjectType>();
		nodeMap.put("type", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("nodeId", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("timestamp", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("uid", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		nodeMap.put("user", AllowedBaseObjectTypes.getBaseObjectType("java.lang.String"));
		nodeMap.put("changeset", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		nodeMap.put("latitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		nodeMap.put("longitude", AllowedBaseObjectTypes.getBaseObjectType("java.lang.Number"));
		Map<String, GenericObjectType> nodeTagsMap = new HashMap<String, GenericObjectType>();
		MapObjectType nodeTagsSchema = new MapObjectType(nodeTagsMap);
		nodeMap.put("tags", nodeTagsSchema);

		// two class object strings, must not be "required"
		Map<String, GenericObjectType> type = new HashMap<String, GenericObjectType>();
		type.put("Osm", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType typeSchema = new MapObjectType(type);
		nodeMap.put("objectType", typeSchema);
		Map<String, GenericObjectType> restName = new HashMap<String, GenericObjectType>();
		restName.put("osm", AllowedBaseObjectTypes.getBaseObjectType("Null"));
		MapObjectType restNameSchema = new MapObjectType(restName);
		nodeMap.put("rest_name", restNameSchema);
		MapObjectType nodeMapSchema = new MapObjectType(nodeMap);

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
