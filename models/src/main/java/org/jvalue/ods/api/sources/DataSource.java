package org.jvalue.ods.api.sources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;
import org.jvalue.ods.api.jsonapi.JsonApiIdentifiableWithMetaData;

import javax.validation.constraints.NotNull;


public final class DataSource extends AbstractDataSource {

	@NotNull private final String id;

	public DataSource(
			@JsonProperty("id") String id,
			@JsonProperty("domainIdKey") JsonPointer domainIdKey,
			@JsonProperty("schema") JsonNode schema,
			@JsonProperty("metaData") DataSourceMetaData metaData) {

		super(domainIdKey, schema, metaData);
		this.id = id;
	}


	@Override
	public String getId() {
		return id;
	}


	@Override
	public String getType() {
		return DataSource.class.getSimpleName();
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (!(other instanceof DataSource)) return false;
		DataSource description = (DataSource) other;
		return  Objects.equal(id, description.id);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, super.hashCode());
	}

}
