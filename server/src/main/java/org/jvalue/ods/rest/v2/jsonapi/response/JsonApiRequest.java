package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.Map;

@JsonRootName(value = "data")
public class JsonApiRequest {

	private String type;
	private String id;
	private Map<String, Object> attributes;

	public JsonApiRequest(
		@JsonProperty("type") String type,
		@JsonProperty("id") String id,
		@JsonProperty("attributes") Map<String, Object> attributes) {

		this.type = type;
		this.id = id;
		this.attributes = attributes;
	}


	public String getType() {
		return type;
	}


	public String getId() {
		return id;
	}


	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
