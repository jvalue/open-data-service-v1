package org.jvalue.ods.api.sources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;


public final class DataSource {

	private final String id;
	private final String domainIdKey;
	private final JsonNode schema;
	private final DataSourceMetaData metaData;

	public DataSource(
			@JsonProperty("id") String id,
			@JsonProperty("domainIdKey") String domainIdKey,
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


	public String getDomainIdKey() {
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
