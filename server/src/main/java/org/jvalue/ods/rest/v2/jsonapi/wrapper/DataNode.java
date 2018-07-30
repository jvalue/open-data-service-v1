package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.ods.api.data.Data;

import java.io.IOException;
import java.util.Map;

import static org.jvalue.ods.utils.JsonUtils.getMapFromJson;

public class DataNode implements JsonApiIdentifiable{

	private final String id;
	private Map<String, Object> attributes;

	private DataNode(JsonNode jsonNode) {
		this.id = jsonNode.get("gaugeId").textValue();
		try {
			this.attributes = getMapFromJson(jsonNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String getId() {
		return id;
	}


	@Override
	public String getType() {
		return Data.class.getSimpleName();
	}


	@JsonAnyGetter
	@JsonIgnoreProperties({"_id","_rev"})
	public Map<String, Object> getAttributes() {
		return attributes;
	}


	public static DataNode from(JsonNode node) {
		return new DataNode(node);
	}
}
