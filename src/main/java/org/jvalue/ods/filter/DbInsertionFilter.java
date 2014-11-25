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
package org.jvalue.ods.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.SourceDataRepository;
import org.jvalue.ods.utils.Assert;


final class DbInsertionFilter implements Filter<ArrayNode, ArrayNode> {

	private final SourceDataRepository dataRepository;
	private final DataSource source;

	@Inject
	DbInsertionFilter(
			@Assisted SourceDataRepository dataRepository,
			@Assisted DataSource source) {

		Assert.assertNotNull(source);
		this.dataRepository = dataRepository;
		this.source = source;
	}


	@Override
	public ArrayNode filter(ArrayNode data) {
		for (JsonNode node : data) {
			String domainKey = source.getDomainIdKey().getProperty(node).asText();
			JsonNode oldNode = dataRepository.findByDomainId(domainKey);
			if (oldNode == null) {
				dataRepository.add(node);
			}
			else {
				ObjectNode objectNode = (ObjectNode) node;
				objectNode.put("_id", oldNode.get("_id").asText());
				objectNode.put("_rev", oldNode.get("_rev").asText());
				dataRepository.update(objectNode);
			}
		}
		return data;
	}

}
