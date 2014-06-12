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

public class OsmSource extends DataSource {

	private static final String sourceId = "org-openstreetmap";
	private static final String url = "/nbgcity.osm";

	private static final ListObjectType sourceSchema;
	private static final MapObjectType dbSchema;

	private static final OdsMetaData metaData = new JacksonMetaData(
			sourceId,
			"openstreetmap",
			"OpenStreetMap Community",
			"http://www.openstreetmap.org",
			"OpenStreetMap ist eine Karte der Welt, erstellt von Menschen wie dir und frei verwendbar unter einer offenen Lizenz.",
			"http://www.openstreetmap.org",
			"http://www.openstreetmap.org/copyright");

	private static final List<OdsView> odsViews = new LinkedList<OdsView>();

	// db schema
	static {

		Map<String, GenericValueType> osmAttributes = new HashMap<String, GenericValueType>();
		osmAttributes.put("type", VALUETYPE_STRING);
		osmAttributes.put("nodeId", VALUETYPE_STRING);
		osmAttributes.put("timestamp", VALUETYPE_STRING);
		osmAttributes.put("uid", VALUETYPE_NUMBER);
		osmAttributes.put("user", VALUETYPE_STRING);
		osmAttributes.put("changeset", VALUETYPE_NUMBER);
		osmAttributes.put("latitude", VALUETYPE_NUMBER);
		osmAttributes.put("longitude", VALUETYPE_NUMBER);

		Map<String, ObjectType> osmReferencedObjects = new HashMap<String, ObjectType>();

		Map<String, GenericValueType> nodeTagsMap = new HashMap<String, GenericValueType>();

		MapObjectType nodeTagsType = new MapObjectType("de-osm-data-tags",
				nodeTagsMap, null);

		osmReferencedObjects.put("tags", nodeTagsType);

		// two class object strings, must not be "required"
		Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
		type.put("Osm", VALUETYPE_NULL);

		ObjectType typeType = new MapObjectType("objectType", type, null);

		osmReferencedObjects.put("objectType", typeType);

		Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
		restName.put("osm", VALUETYPE_NULL);

		ObjectType restNameType = new MapObjectType("rest_name", restName, null);

		osmReferencedObjects.put("rest_name", restNameType);

		MapObjectType osmType = new MapObjectType("de-osm-data", osmAttributes,
				osmReferencedObjects);

		dbSchema = osmType;

	}

	// source schema
	static {
		List<ObjectType> stationList = new LinkedList<ObjectType>();
		stationList.add(dbSchema);
		sourceSchema = new ListObjectType(null, stationList);
	}

	// ods views
	static {
		odsViews.add(new OdsView(
				"_design/osm",
				"getNodeById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.nodeId) emit( doc.nodeId, doc)}"));

		odsViews.add(new OdsView("_design/osm", "getWayById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.wayId) emit( doc.wayId, doc)}"));

		odsViews.add(new OdsView(
				"_design/osm",
				"getRelationById",
				"function(doc) { if(doc.dataType == 'Osm' && doc.relationId) emit( doc.relationId, doc)}"));

		odsViews.add(new OdsView(
				"_design/osm",
				"getOsmDataById",
				"function(doc) { if (doc.dataType == 'Osm' && doc.nodeId) {emit(doc.nodeId, doc)} else if (doc.wayId) { emit(doc.wayId, doc)} else if (doc.relationId) { emit(doc.relationId, doc)}}"));

		odsViews.add(new OdsView(
				"_design/osm",
				"getDocumentsByKeyword",
				"function(doc) { if(doc.dataType == 'Osm' && doc.tags){ for (var i in doc.tags) { emit(doc.tags[i], doc) }} }"));

		odsViews.add(new OdsView("_design/osm", "getMetadata",
				"function(doc) { if(doc.title == 'openstreetmap') emit(null, doc)}"));

		odsViews.add(new OdsView(
				"_design/osm",
				"getCouchIdByOsmId",
				"function(doc) { if (doc.dataType == 'Osm' && doc.nodeId) {emit(doc.nodeId, doc._id)} else if (doc.wayId) { emit(doc.wayId, doc._id)} else if (doc.relationId) { emit(doc.relationId, doc._id)}}"));
		odsViews.add(new OdsView("_design/osm", "getClassObject",
				"function(doc) { if(doc.name == 'de-osm-data') emit (null, doc) }"));

		odsViews.add(new OdsView("_design/osm", "getClassObjectId",
				"function(doc) { if(doc.name == 'de-osm-data') emit (null, doc._id) }"));
	}

	public static final OsmSource INSTANCE = new OsmSource();

	protected OsmSource() {
		super(sourceId, url, sourceSchema, dbSchema, metaData, odsViews);
	}
}
