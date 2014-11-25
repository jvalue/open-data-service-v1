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

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Provider;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.objecttypes.ListObjectType;
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.db.DbView;
import org.jvalue.ods.db.SourceDataRepository;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.filter.FilterChainElement;
import org.jvalue.ods.filter.FilterFactory;
import org.jvalue.ods.filter.adapter.SourceAdapterFactory;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NULL;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NUMBER;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_STRING;

final class OsmConfiguration {

	private final FilterFactory filterFactory;
	private final SourceAdapterFactory sourceAdapterFactory;
	private final Provider<Filter<JsonNode, Object>> translatorProvider;

	@Inject
	public OsmConfiguration(
			FilterFactory filterFactory,
			SourceAdapterFactory sourceAdapterFactory,
			Provider<Filter<JsonNode, Object>> translatorProvider) {

		this.filterFactory = filterFactory;
		this.sourceAdapterFactory = sourceAdapterFactory;
		this.translatorProvider = translatorProvider;
	}


	public DataSource getDataSource() {
		String sourceId = "org-openstreetmap";
		String url = "/nbgcity.osm";

		ListObjectType sourceSchema;
		MapObjectType dbSchema;

		OdsMetaData metaData = new JacksonMetaData(
				sourceId,
				"openstreetmap",
				"OpenStreetMap Community",
				"http://www.openstreetmap.org",
				"OpenStreetMap ist eine Karte der Welt, erstellt von Menschen wie dir und frei verwendbar unter einer offenen Lizenz.",
				"http://www.openstreetmap.org",
				"http://www.openstreetmap.org/copyright");

		List<DbView> dbViews = new LinkedList<DbView>();

		// db schema
		{

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

			ObjectType restNameType = new MapObjectType("restName", restName,
					null);

			osmReferencedObjects.put("restName", restNameType);

			MapObjectType osmType = new MapObjectType("de-osm-data",
					osmAttributes, osmReferencedObjects);

			dbSchema = osmType;
		}

		// source schema
		{
			List<ObjectType> stationList = new LinkedList<ObjectType>();
			stationList.add(dbSchema);
			sourceSchema = new ListObjectType(null, stationList);
		}

		// ods views
		{
		/*

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

*/
			// return new DataSource(sourceId, url, sourceSchema, dbSchema, dbSchema, metaData, odsViews);
			return null;
		}
	}

	public FilterChainElement<Void, ?> getFilterChain() {
		DataSource source = getDataSource();

		FilterChainElement<Void, File> chain = FilterChainElement.instance(
				sourceAdapterFactory.createFileSourceAdapter(source));

		chain
				.setNextFilter(new OsmAdapter());
				// .setNextFilter(new DataAdditionFilter(source))
				// .setNextFilter(filterFactory.createDbInsertionFilter(source))
				// .setNextFilter(filterFactory.createNotificationFilter(source));

		return chain;
	}


	public SourceDataRepository getDataRepository() {
		return null;
	}

}
