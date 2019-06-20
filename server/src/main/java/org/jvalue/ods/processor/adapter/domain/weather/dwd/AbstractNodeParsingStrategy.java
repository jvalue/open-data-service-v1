package org.jvalue.ods.processor.adapter.domain.weather.dwd;

import com.fasterxml.jackson.databind.JsonNode;

abstract class AbstractNodeParsingStrategy implements NodeParsingStrategy {

	/**
	 * @return the JsonNode where the "name" field is set to name.
	 */
	protected JsonNode findDataPointNodeByName(JsonNode node, String name) {
		for (JsonNode subNode : node) {
			if (name.equals(subNode.get("name").asText())) {
				return subNode;
			}
		}
		return null;
	}
}
