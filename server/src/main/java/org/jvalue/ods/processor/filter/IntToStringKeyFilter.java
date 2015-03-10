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
package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.sources.DataSource;


final class IntToStringKeyFilter extends AbstractFilter<ObjectNode, ObjectNode> {

	private final JsonPointer parentPointer;
	private final String keyName;

	@Inject
	IntToStringKeyFilter(
			@Assisted DataSource source,
			MetricRegistry registry) {

		super(source, registry);

		String idPointer = source.getDomainIdKey().toString();
		this.parentPointer = JsonPointer.compile(idPointer.substring(0, idPointer.lastIndexOf('/')));
		this.keyName = idPointer.substring(idPointer.lastIndexOf('/') + 1, idPointer.length());
	}


	@Override
	protected ObjectNode doFilter(ObjectNode node) throws FilterException {
		JsonNode keyNode = node.at(source.getDomainIdKey());
		if (keyNode.isTextual()) return node;
		JsonNode parentNode = node.at(parentPointer);

		if (parentNode.isObject()) {
			// add to parent object
			((ObjectNode) parentNode).put(keyName, keyNode.asText());
		} else if (parentNode.isArray()) {
			// add to parent array
			((ArrayNode) parentNode).insert(Integer.valueOf(keyName), keyNode.asText());
		} else {
			throw new IllegalArgumentException("failed to find parent node for domainIdKey");
		}

		return node;
	}


	@Override
	protected void doOnComplete() throws FilterException {
		// nothing to do
	}

}
