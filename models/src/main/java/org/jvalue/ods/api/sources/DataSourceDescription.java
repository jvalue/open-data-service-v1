package org.jvalue.ods.api.sources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

import org.jvalue.ods.api.utils.JsonPointerDeserializer;
import org.jvalue.ods.api.utils.JsonPointerSerializer;

import javax.validation.constraints.NotNull;


public class DataSourceDescription {

	@JsonSerialize(using = JsonPointerSerializer.class)
	@JsonDeserialize(using = JsonPointerDeserializer.class)
	@NotNull private final JsonPointer domainIdKey;
	@NotNull private final JsonNode schema;
	@NotNull private final DataSourceMetaData metaData;

	public DataSourceDescription(
			@JsonProperty("domainIdKey") JsonPointer domainIdKey,
			@JsonProperty("schema") JsonNode schema,
			@JsonProperty("metaData") DataSourceMetaData metaData) {

		this.domainIdKey = domainIdKey;
		this.schema = schema;
		this.metaData = metaData;
	}


	public JsonNode getSchema() {
		return schema;
	}


	public DataSourceMetaData getMetaData() {
		return metaData;
	}


	public JsonPointer getDomainIdKey() {
		return domainIdKey;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSourceDescription)) return false;
		if (other == this) return true;
		DataSourceDescription description = (DataSourceDescription) other;
		return Objects.equal(domainIdKey, description.domainIdKey)
				&& Objects.equal(schema, description.schema)
				&& Objects.equal(domainIdKey, description.domainIdKey)
				&& Objects.equal(metaData, description.metaData);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(domainIdKey, schema, metaData);
	}

}
