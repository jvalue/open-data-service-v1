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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.objecttypes.ListObjectType;
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.data.valuetypes.GenericValueType;
import org.jvalue.ods.data.valuetypes.SimpleValueType;
import org.jvalue.ods.utils.Log;
import org.jvalue.ods.filter.translator.Translator;
import org.jvalue.ods.utils.Assert;



final class PegelPortalMvTranslator extends Translator<String> {

	private static final Map<String, Integer> tableMapping = new HashMap<String, Integer>();
	static {
		tableMapping.put(PegelPortalMvConfiguration.KEY_STATION, 0);
		tableMapping.put(PegelPortalMvConfiguration.KEY_WATER, 1);
		tableMapping.put(PegelPortalMvConfiguration.KEY_TIMESTAMP, 2);
		tableMapping.put(PegelPortalMvConfiguration.KEY_LEVEL, 3);
		tableMapping.put(PegelPortalMvConfiguration.KEY_EFFLUENT, 4);
		tableMapping.put(PegelPortalMvConfiguration.KEY_AGENCY, 8);
	}

	
	private final DataSource source;

	public PegelPortalMvTranslator(DataSource source) {
		Assert.assertNotNull(source);
		this.source = source;
	}


	@Override
	public Object translate(String httpContent) {
		Document doc = Jsoup.parse(httpContent);


		Elements header = doc.select("#pegeltab thead tr");
		Elements body = doc.select("#pegeltab tbody tr");

		if (header.isEmpty() || body.isEmpty())
			return null;
		if (header.size() > 1)
			return unknownFormat();

		try {

			Map<String, GenericValueType> mapValueTypes = ((MapObjectType) ((ListObjectType) 
						source.getDataSourceSchema())
					.getReferencedObjects().get(0)).getAttributes();

			List<Object> objectList = new LinkedList<Object>();
			for (Element row : body) {
				Map<String, Object> objectMap = new HashMap<String, Object>();
				for (Map.Entry<String, Integer> e : tableMapping.entrySet()) {

					String key = e.getKey();
					int colIdx = e.getValue();
					SimpleValueType type = (SimpleValueType) mapValueTypes
							.get(key);
					String value = extractText(row.child(colIdx));
					if (value.equals(""))
						continue;

					objectMap.put(
							key,
							createValue(value, type.getName()));
				}

				objectMap.put(PegelPortalMvConfiguration.KEY_LEVEL_UNIT, "cm ü PNP");
				objectMap.put(PegelPortalMvConfiguration.KEY_EFFLUENT_UNIT, "m³/s");

				objectList.add(objectMap);
			}

			return objectList;
		} catch (Exception e) {
			return unknownFormat();
		}
	}

	private Object createValue(String value, String type) {
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

	private Object unknownFormat() {
		String error = "Unknown html format found while parsing source";
		Log.error(error);
		System.err.println(error);
		return null;
	}
}
