package org.jvalue.ods.rest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

final class JsonDbPropertyFilter {

	public List<JsonNode> filter(List<JsonNode> nodes) {
		for (JsonNode node : nodes) filter(node);
		return nodes;
	}


	public JsonNode filter(JsonNode node) {
		ObjectNode objectNode = (ObjectNode) node;
		objectNode.remove("_id");
		objectNode.remove("_rev");
		return objectNode;
	}

}
