package org.jvalue.ods.db.mongodb.repositories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.commons.EntityBase;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class JsonNodeEntity implements EntityBase {

	@NotNull
	private final JsonNode node;


	public JsonNodeEntity(@JsonProperty("node") JsonNode node){
		this.node = node;
	}


	@Override
	public String getId() {
		return "default";
	}


	public JsonNode getNode() {
		return node;
	}
}
