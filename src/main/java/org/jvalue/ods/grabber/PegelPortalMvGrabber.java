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

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.metadata.JacksonMetaData;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.schema.GenericValueType;
import org.jvalue.ods.data.schema.ListComplexValueType;
import org.jvalue.ods.data.schema.MapComplexValueType;
import org.jvalue.ods.data.schema.SimpleValueType;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.Grabber;
import org.jvalue.ods.translator.HtmlTranslator;

/**
 * The Class MecklenburgVorpommernPegel
 */
public class PegelPortalMvGrabber implements Grabber {

	private static final String
		KEY_STATION = "station",
		KEY_WATER = "water",
		KEY_TIMESTAMP = "timestamp",
		KEY_LEVEL = "level",
		KEY_LEVEL_UNIT = "levelUnit",
		KEY_EFFLUENT = "effluent",
		KEY_EFFLUENT_UNIT = "effluentUnit",
		KEY_AGENCY = "agency";


	private final DataSource dataSource = new DataSource(
			"http://www.pegelportal-mv.de/pegel_list.html",
			getDataSourceSchema(),
			new JacksonMetaData(
				"de-pegelportal-mv",
				"pegelportal-mv",
				"Landesamt für Umwelt, Naturschutz und Geologie",
				"http://www.pegelportal-mv.de/impressum.html",
				"Pegelportal stellt kostenfrei tagesaktuelle Rohwerte verschiedener "
					+ "gewässerkundlicher Parameter (z.B. Wasserstand) der Binnen- und "
					+ "Küstenpegel des Bundeslandes Mecklenburg-Vorpommern zur Ansicht bereit",
				"http://www.pegelportal-mv.de/",
				"http://www.pegelportal-mv.de/impressum.html"));




	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#grab()
	 */
	@Override
	public GenericEntity grab() {
		Translator translator = new PegelTranslator();
		return (ListObject) translator.translate(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDataSourceSchema()
	 */
	@Override
	public GenericValueType getDataSourceSchema() {
		MapComplexValueType type = getDbSchema();
		List<GenericValueType> stationList = new LinkedList<GenericValueType>();
		stationList.add(type);
		return new ListComplexValueType(stationList);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.grabber.Grabber#getDbSchema()
	 */
	@Override
	public MapComplexValueType getDbSchema() {
		Map<String, GenericValueType> object = new HashMap<String, GenericValueType>();
		object.put(KEY_STATION, VALUETYPE_STRING);
		object.put(KEY_WATER, VALUETYPE_STRING);
		object.put(KEY_TIMESTAMP, VALUETYPE_STRING);
		object.put(KEY_LEVEL, VALUETYPE_NUMBER);
		object.put(KEY_LEVEL_UNIT, VALUETYPE_STRING);
		object.put(KEY_EFFLUENT, VALUETYPE_NUMBER);
		object.put(KEY_EFFLUENT_UNIT, VALUETYPE_STRING);
		object.put(KEY_AGENCY, VALUETYPE_STRING);


		Map<String, GenericValueType> type = new HashMap<String, GenericValueType>();
		type.put("MV-Station", VALUETYPE_NULL);
		object.put("objectType", new MapComplexValueType(type));

		Map<String, GenericValueType> restName = new HashMap<String, GenericValueType>();
		restName.put("mv-stations", VALUETYPE_NULL);
		object.put("rest_name", new MapComplexValueType(restName));

		return new MapComplexValueType(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#getMetaData()
	 */
	@Override
	public OdsMetaData getMetaData() {
		return dataSource.getMetaData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Grabber#getOdsViews()
	 */
	@Override
	public List<OdsView> getOdsViews() {
		List<OdsView> list = new LinkedList<>();

		list.add(new OdsView("_design/pegelportal-mv", "getSingleStation",
				"function(doc) { if(doc.dataType == 'MV-Station') emit(doc.station, doc)}"));

		list.add(new OdsView("_design/pegelportal-mv", "getMetadata",
				"function(doc) { if(doc.title == 'pegelportal-mv') emit(null, doc)}"));

		list.add(new OdsView("_design/pegelportal-mv", "getAllStationsFlat",
				"function(doc) { if(doc.dataType == 'MV-Station') emit (null, doc.station) }"));
		list.add(new OdsView("_design/pegelportal-mv", "getAllStations",
				"function(doc) { if(doc.dataType == 'MV-Station') emit (null, doc) }"));
		list.add(new OdsView("_design/pegelportal-mv", "getStationId",
				"function(doc) { if(doc.dataType == 'MV-Station') emit (doc.station, doc._id) }"));

		list.add(new OdsView("_design/pegelportal-mv", "getClassObject",
				"function(doc) { if(doc.rest_name[\"mv-stations\"]) emit (null, doc) }"));

		list.add(new OdsView("_design/pegelportal-mv", "getClassObjectId",
				"function(doc) { if(doc.rest_name[\"mv-stations\"]) emit (null, doc._id) }"));

		return list;
	}



	private static class PegelTranslator extends HtmlTranslator {

		static final Map<String, Integer> tableMapping = new HashMap<String, Integer>();
		static {
			tableMapping.put(KEY_STATION, 0);
			tableMapping.put(KEY_WATER, 1);
			tableMapping.put(KEY_TIMESTAMP, 2);
			tableMapping.put(KEY_LEVEL, 3);
			tableMapping.put(KEY_EFFLUENT, 4);
			tableMapping.put(KEY_AGENCY, 8);
		}


		@Override
		protected GenericEntity translateHelper(Document doc, GenericValueType valueTypes) {
			Elements header = doc.select("#pegeltab thead tr");
			Elements body = doc.select("#pegeltab tbody tr");

			if (header.isEmpty() || body.isEmpty()) return null;
			if (header.size() > 1) return unknownFormat();

			try {
				Map<String, GenericValueType> mapValueTypes 
					= ((MapComplexValueType) ((ListComplexValueType) valueTypes).getList().get(0))
					.getMap();

				List<Serializable> objectList = new LinkedList<Serializable>();
				for (Element row : body) {
					MapObject objectMap = new MapObject();
					for (Map.Entry<String, Integer> e : tableMapping.entrySet()) {

						String key = e.getKey();
						int colIdx = e.getValue();
						SimpleValueType type = (SimpleValueType) mapValueTypes.get(key);
						String value = extractText(row.child(colIdx));
						if (value.equals("")) continue;

						objectMap.getMap().put(key, new BaseObject(createValue(value, type.getName())));
					}

					objectMap.getMap().put(KEY_LEVEL_UNIT, new BaseObject("cm ü PNP"));
					objectMap.getMap().put(KEY_EFFLUENT_UNIT, new BaseObject("m³/s"));

					objectList.add(objectMap);
				}

				return new ListObject(objectList);
			} catch (Exception e) {
				return unknownFormat();
			}
		}


		private Serializable createValue(String value, String type) {
			if (type.equals("java.lang.String")) return StringEscapeUtils.unescapeHtml4(value);
			else if (type.equals("java.lang.Number")) return new Double(value);
			else throw new IllegalArgumentException("Unknown type " + type);
		}

		private String extractText(Element element) {
			StringBuilder builder = new StringBuilder();
			for (Node node : element.childNodes()) {
				if (node instanceof TextNode) {
					builder.append(node.toString());
				} else if (node instanceof Element) {
					builder.append(extractText((Element) node));
				}
			}
			return builder.toString();
		}

		
		private GenericEntity unknownFormat() {
			String error = "Unknown html format found while parsing source";
			Logging.error(this.getClass(), error);
			System.err.println(error);
			return null;
		}
	}
}
