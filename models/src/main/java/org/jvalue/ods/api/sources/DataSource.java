package org.jvalue.ods.api.sources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

import org.jvalue.ods.api.utils.JsonPointerDeserializer;
import org.jvalue.ods.api.utils.JsonPointerSerializer;


public final class DataSource {

	private final String id;
	@JsonSerialize(using = JsonPointerSerializer.class)
	@JsonDeserialize(using = JsonPointerDeserializer.class)
	private final JsonPointer domainIdKey;
	private final JsonNode schema;
	private final DataSourceMetaData metaData;

	public DataSource(
			@JsonProperty("id") String id,
			@JsonProperty("domainIdKey") JsonPointer domainIdKey,
			@JsonProperty("schema") JsonNode schema,
			@JsonProperty("metaData") DataSourceMetaData metaData) {

		this.id = id;
		this.domainIdKey = domainIdKey;
		this.schema = schema;
		this.metaData = metaData;
	}


	public String getId() {
		return id;
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
		if (other == null || !(other instanceof DataSource)) return false;
		if (other == this) return true;
		DataSource description = (DataSource) other;
		return  Objects.equal(id, description.id)
				&& Objects.equal(domainIdKey, description.domainIdKey)
				&& Objects.equal(schema, description.schema)
				&& Objects.equal(domainIdKey, description.domainIdKey)
				&& Objects.equal(metaData, description.metaData);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, domainIdKey, schema, metaData);
	}

}
