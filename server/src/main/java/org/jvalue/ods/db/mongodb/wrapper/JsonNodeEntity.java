package org.jvalue.ods.db.mongodb.wrapper;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;
import org.jvalue.commons.EntityBase;

import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonNodeEntity implements EntityBase {

	@NotNull private final String id;
	@NotNull private final JsonNode value;

	@JsonCreator
	public JsonNodeEntity(
		@JsonProperty("id") String id,
		@JsonProperty("value") JsonNode value) {

		this.id = id;
		this.value = value;
	}


	public String getId() {
		return id;
	}

	public JsonNode getValue() {
		return value;
	}

	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (!(other instanceof JsonNodeEntity)) return false;
		JsonNodeEntity jsonNode = (JsonNodeEntity) other;
		return Objects.equal(id, jsonNode.id);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, super.hashCode());
	}

}
