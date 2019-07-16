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

	SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.domain;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.adapter.AbstractSourceAdapter;
import org.jvalue.ods.processor.adapter.SourceAdapterException;
import org.jvalue.ods.processor.adapter.SourceIterator;
import org.jvalue.ods.utils.HttpUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public final class PegelPortalMvSourceAdapter extends AbstractSourceAdapter {

	private static final String KEY_STATION = "station", KEY_WATER = "water",
			KEY_TIMESTAMP = "timestamp", KEY_LEVEL = "level",
			KEY_LEVEL_UNIT = "levelUnit", KEY_EFFLUENT = "effluent",
			KEY_EFFLUENT_UNIT = "effluentUnit", KEY_AGENCY = "agency";

	private static final Map<String, Integer> tableMapping = new HashMap<>();
	static {
		tableMapping.put(KEY_STATION, 0);
		tableMapping.put(KEY_WATER, 1);
		tableMapping.put(KEY_TIMESTAMP, 2);
		tableMapping.put(KEY_LEVEL, 3);
		tableMapping.put(KEY_EFFLUENT, 4);
		tableMapping.put(KEY_AGENCY, 8);
	}

	private static final Map<String, Class<?>> schema = new HashMap<>();
	static {
		schema.put(KEY_STATION, String.class);
		schema.put(KEY_WATER, String.class);
		schema.put(KEY_TIMESTAMP, String.class);
		schema.put(KEY_LEVEL, Number.class);
		schema.put(KEY_LEVEL_UNIT, String.class);
		schema.put(KEY_EFFLUENT, Number.class);
		schema.put(KEY_EFFLUENT_UNIT, String.class);
		schema.put(KEY_AGENCY, String.class);
	}
	

	@Inject
	PegelPortalMvSourceAdapter(
			@Assisted DataSource source,
			@Assisted String sourceUrl,
			MetricRegistry registry) {

		super(source, sourceUrl, registry);
	}


	@Override
	protected SourceIterator doCreateIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
		return new PegelPortalMvIterator(source, sourceUrl, registry);
	}

	private static final class PegelPortalMvIterator extends SourceIterator {

		private final Iterator<Element> rowIterator;
		private int currentRowIdx = 0;

		public PegelPortalMvIterator(DataSource source, URL sourceUrl, MetricRegistry registry) {
			super(source, sourceUrl, registry);

			try {
				String httpContent = HttpUtils.readUrl(sourceUrl, "UTF-8");
				this.rowIterator = Jsoup.parse(httpContent).select("#pegeltab tbody tr").iterator();
			} catch (IOException ioe) {
				throw new SourceAdapterException(ioe);
			}

		}

		@Override
		protected boolean doHasNext() {
			return rowIterator.hasNext();
		}


		@Override
		protected ObjectNode doNext() {
			ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
			Element row = rowIterator.next();

			for (Map.Entry<String, Integer> entry : tableMapping.entrySet()) {
				String key = entry.getKey();
				int colIdx = entry.getValue();

				Class<?> type = schema.get(entry.getKey());
				String value = extractText(row.child(colIdx));

				if (value.equals("")) continue;
				if (type.equals(String.class)) objectNode.put(key, StringEscapeUtils.unescapeHtml4(value));
				else if (type.equals(Number.class)) objectNode.put(key, new Double(value));
				else throw new SourceAdapterException("Unknown type " + type.getName());
			}

			objectNode.put(KEY_LEVEL_UNIT, "cm \u00fc PNP");
			objectNode.put(KEY_EFFLUENT_UNIT, "m\u00b3/s");
			objectNode.put("id", String.valueOf(currentRowIdx++)); // website does not contain any one row with unique values --> combined keys required
			return objectNode;
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

	}

}
