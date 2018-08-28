package org.jvalue.ods.rest.v2.jsonapi.response;

import com.fasterxml.jackson.annotation.*;

import java.util.Map;
import java.util.Objects;

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


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JsonApiRequest that = (JsonApiRequest) o;
		return Objects.equals(type, that.type) &&
			Objects.equals(id, that.id) &&
			Objects.equals(attributes, that.attributes);
	}


	@Override
	public int hashCode() {
		return Objects.hash(type, id, attributes);
	}
}
