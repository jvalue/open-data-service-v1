package org.jvalue.ods.api.sources;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;

import org.jvalue.ods.api.utils.JsonPointerDeserializer;
import org.jvalue.ods.api.utils.JsonPointerSerializer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


abstract class AbstractDataSource {

	@JsonSerialize(using = JsonPointerSerializer.class)
	@JsonDeserialize(using = JsonPointerDeserializer.class)
	@NotNull private final JsonPointer domainIdKey;
	@NotNull private final JsonNode schema;
	@Valid @NotNull private final DataSourceMetaData metaData;

	protected AbstractDataSource(
			JsonPointer domainIdKey,
			JsonNode schema,
			DataSourceMetaData metaData) {

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
		if (other == null || !(other instanceof AbstractDataSource)) return false;
		if (other == this) return true;
		AbstractDataSource description = (AbstractDataSource) other;
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
