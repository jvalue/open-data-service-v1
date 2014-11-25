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
import org.jvalue.ods.utils.JsonPropertyKey;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NULL;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_NUMBER;
import static org.jvalue.ods.data.valuetypes.AllowedValueTypes.VALUETYPE_STRING;

public final class PegelPortalMvConfigurationFactory {

	// keys used in the json schema
	public static final String KEY_STATION = "station", KEY_WATER = "water",
			KEY_TIMESTAMP = "timestamp", KEY_LEVEL = "level",
			KEY_LEVEL_UNIT = "levelUnit", KEY_EFFLUENT = "effluent",
			KEY_EFFLUENT_UNIT = "effluentUnit", KEY_AGENCY = "agency";


	private final DataSourceConfiguration configuration;

	@Inject
	public PegelPortalMvConfigurationFactory(
			DbFactory dbFactory,
			FilterFactory filterFactory) {

		DataSource dataSource = createDataSource();
		SourceDataRepository dataRepository = dbFactory.createSourceDataRepository("pegelportalMv", dataSource.getDomainIdKey());
		FilterChainElement<Void, ?> filterChain = createFilterChain(dataSource, dataRepository, filterFactory);
		this.configuration = new DataSourceConfiguration(dataSource, filterChain, dataRepository);
	}


	public DataSourceConfiguration createConfiguration() {
		return configuration;
	}


	private DataSource createDataSource() {

		String sourceId = "de-pegelportal-mv";
		String url = "http://www.pegelportal-mv.de/pegel_list.html";

		ListObjectType sourceSchema;
		MapObjectType dbSchema;

		OdsMetaData metaData = new JacksonMetaData(
				sourceId,
				"pegelportal-mv",
				"Landesamt für Umwelt, Naturschutz und Geologie",
				"http://www.pegelportal-mv.de/impressum.html",
				"Pegelportal stellt kostenfrei tagesaktuelle Rohwerte verschiedener "
						+ "gewässerkundlicher Parameter (z.B. Wasserstand) der Binnen- und "
						+ "Küstenpegel des Bundeslandes Mecklenburg-Vorpommern zur Ansicht bereit",
				"http://www.pegelportal-mv.de/",
				"http://www.pegelportal-mv.de/impressum.html");

		List<OdsView> odsViews = new LinkedList<OdsView>();

		// db schema
		{
			Map<String, GenericValueType> ppAttributes = new HashMap<String, GenericValueType>();
			ppAttributes.put(KEY_STATION, VALUETYPE_STRING);
			ppAttributes.put(KEY_WATER, VALUETYPE_STRING);
			ppAttributes.put(KEY_TIMESTAMP, VALUETYPE_STRING);
			ppAttributes.put(KEY_LEVEL, VALUETYPE_NUMBER);
			ppAttributes.put(KEY_LEVEL_UNIT, VALUETYPE_STRING);
			ppAttributes.put(KEY_EFFLUENT, VALUETYPE_NUMBER);
			ppAttributes.put(KEY_EFFLUENT_UNIT, VALUETYPE_STRING);
			ppAttributes.put(KEY_AGENCY, VALUETYPE_STRING);

			Map<String, ObjectType> ppReferencedObjects = new HashMap<String, ObjectType>();

			Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
			type.put("MV-Station", VALUETYPE_NULL);

			ObjectType typeType = new MapObjectType("objectType", type, null);

			ppReferencedObjects.put("objectType", typeType);

			Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
			restName.put("mv-stations", VALUETYPE_NULL);

			ObjectType restNameType = new MapObjectType("restName", restName,
					null);

			ppReferencedObjects.put("restName", restNameType);

			MapObjectType pegelPortalType = new MapObjectType(
					"de-pegelportal-mv-station", ppAttributes,
					ppReferencedObjects);

			dbSchema = pegelPortalType;
		}

		// source schema
		{
			List<ObjectType> stationList = new LinkedList<ObjectType>();
			stationList.add(dbSchema);
			sourceSchema = new ListObjectType(null, stationList);
		}

		return new DataSource(
				sourceId,
				url,
				sourceSchema,
				dbSchema,
				dbSchema,
				metaData,
				odsViews,
				new JsonPropertyKey.Builder().stringPath("station").build());
	}


	private FilterChainElement<Void, ?> createFilterChain(DataSource dataSource, SourceDataRepository dataRepository, FilterFactory filterFactory) {


		FilterChainElement<Void, ArrayNode> chain = FilterChainElement.instance(
				new PegelPortalMvAdapter(dataSource));

		chain.setNextFilter(filterFactory.createDbInsertionFilter(dataSource, dataRepository))
				.setNextFilter(filterFactory.createNotificationFilter(dataSource));

		return chain;

	}

}
