package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;

@JsonTypeName(value = "data")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
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
