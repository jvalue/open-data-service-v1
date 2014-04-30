package org.jvalue.ods.grabber;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.ListValue;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.schema.ListSchema;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.data.schema.NullSchema;
import org.jvalue.ods.data.schema.NumberSchema;
import org.jvalue.ods.data.schema.Schema;
import org.jvalue.ods.data.schema.StringSchema;
import org.jvalue.ods.main.Grabber;
import org.jvalue.ods.translator.OsmTranslator;

public class OsmGrabber implements Grabber {

	@Override
	public GenericValue grab() {

		Translator translator = new OsmTranslator();

		Schema schema = getDataSourceSchema();

		ListValue list = (ListValue) translator.translate(new DataSource(
				"/nbgcity.osm", schema));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDataSourceSchema()
	 */
	@Override
	public Schema getDataSourceSchema() {
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

		List<Schema> entityList = new LinkedList<Schema>();
		entityList.add(nodeMapSchema);
		ListSchema listSchema = new ListSchema(entityList);

		return listSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDbSchema()
	 */
	@Override
	public MapSchema getDbSchema() {
		Map<String, Schema> nodeMap = new HashMap<String, Schema>();
		nodeMap.put("type", new StringSchema());
		nodeMap.put("nodeId", new StringSchema());
		nodeMap.put("timestamp", new StringSchema());
		nodeMap.put("uid", new NumberSchema());
		nodeMap.put("user", new StringSchema());
		nodeMap.put("changeset", new NumberSchema());
		nodeMap.put("latitude", new NumberSchema());
		nodeMap.put("longitude", new NumberSchema());
		Map<String, Schema> nodeTagsMap = new HashMap<String, Schema>();
		MapSchema nodeTagsSchema = new MapSchema(nodeTagsMap);
		nodeMap.put("tags", nodeTagsSchema);

		// two class object strings, must not be "required"
		Map<String, Schema> type = new HashMap<String, Schema>();
		type.put("Osm", new NullSchema());
		MapSchema typeSchema = new MapSchema(type);
		nodeMap.put("objectType", typeSchema);
		Map<String, Schema> restName = new HashMap<String, Schema>();
		restName.put("osm", new NullSchema());
		MapSchema restNameSchema = new MapSchema(restName);
		nodeMap.put("rest_name", restNameSchema);
		MapSchema nodeMapSchema = new MapSchema(nodeMap);

		return nodeMapSchema;
	}

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
