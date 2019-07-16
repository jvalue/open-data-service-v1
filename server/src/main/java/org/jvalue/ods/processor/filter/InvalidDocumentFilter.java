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
package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;

import java.util.Iterator;


/**
 * Filter those elements from an input array which do not represent
 * a valid json object, such as plain text or number nodes.
 */
final class InvalidDocumentFilter extends AbstractFilter<ArrayNode, ArrayNode> {


	@Inject
	InvalidDocumentFilter(@Assisted DataSource source, MetricRegistry registry) {
		super(source, registry);
	}


	@Override
	protected ArrayNode doFilter(ArrayNode data) {
		Iterator<JsonNode> iterator = data.elements();
		while (iterator.hasNext()) {
			if (!iterator.next().isObject()) iterator.remove();
		}
		return data;
	}


	@Override
	protected void doOnComplete() {
		// nothing to do
	}

}
