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
package org.jvalue.ods.translator;

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
import org.jvalue.ods.configuration.PegelPortalMvConfiguration;
import org.jvalue.ods.data.generic.BaseObject;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.objecttypes.ListObjectType;
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.SimpleValueType;
import org.jvalue.ods.logger.Logging;



final class PegelPortalMvTranslator extends HtmlTranslator {

	private static final Map<String, Integer> tableMapping = new HashMap<String, Integer>();
	static {
		tableMapping.put(PegelPortalMvConfiguration.KEY_STATION, 0);
		tableMapping.put(PegelPortalMvConfiguration.KEY_WATER, 1);
		tableMapping.put(PegelPortalMvConfiguration.KEY_TIMESTAMP, 2);
		tableMapping.put(PegelPortalMvConfiguration.KEY_LEVEL, 3);
		tableMapping.put(PegelPortalMvConfiguration.KEY_EFFLUENT, 4);
		tableMapping.put(PegelPortalMvConfiguration.KEY_AGENCY, 8);
	}


	@Override
	protected GenericEntity translateHelper(Document doc,
			ObjectType valueTypes) {
		Elements header = doc.select("#pegeltab thead tr");
		Elements body = doc.select("#pegeltab tbody tr");

		if (header.isEmpty() || body.isEmpty())
			return null;
		if (header.size() > 1)
			return unknownFormat();

		try {

			Map<String, GenericValueType> mapValueTypes = ((MapObjectType) ((ListObjectType) valueTypes)
					.getReferencedObjects().get(0)).getAttributes();

			List<Serializable> objectList = new LinkedList<Serializable>();
			for (Element row : body) {
				MapObject objectMap = new MapObject();
				for (Map.Entry<String, Integer> e : tableMapping.entrySet()) {

					String key = e.getKey();
					int colIdx = e.getValue();
					SimpleValueType type = (SimpleValueType) mapValueTypes
							.get(key);
					String value = extractText(row.child(colIdx));
					if (value.equals(""))
						continue;

					objectMap.getMap().put(
							key,
							new BaseObject(createValue(value,
									type.getName())));
				}

				objectMap.getMap().put(PegelPortalMvConfiguration.KEY_LEVEL_UNIT,
						new BaseObject("cm ü PNP"));
				objectMap.getMap().put(
						PegelPortalMvConfiguration.KEY_EFFLUENT_UNIT,
						new BaseObject("m³/s"));

				objectList.add(objectMap);
			}

			return new ListObject(objectList);
		} catch (Exception e) {
			return unknownFormat();
		}
	}

	private Serializable createValue(String value, String type) {
		if (type.equals("java.lang.String"))
			return StringEscapeUtils.unescapeHtml4(value);
		else if (type.equals("java.lang.Number"))
			return new Double(value);
		else
			throw new IllegalArgumentException("Unknown type " + type);
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
