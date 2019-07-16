/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.sources;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.ods.api.utils.JsonPointerDeserializer;
import org.jvalue.ods.api.utils.JsonPointerSerializer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


abstract class AbstractDataSource {

	@JsonSerialize(using = JsonPointerSerializer.class)
	@JsonDeserialize(using = JsonPointerDeserializer.class)
	@Schema(implementation = String.class, example = "/gaugeId", pattern = "/(.*)", required = true)
	@NotNull private final JsonPointer domainIdKey;
	@Schema(implementation = Object.class, required = true,
		example = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\"}")
	@NotNull private final JsonNode schema;
	@Schema(required = true)
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
