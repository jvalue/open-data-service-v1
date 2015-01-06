package org.jvalue.ods.api.sources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;


public final class DataSourceDescription extends AbstractDataSource {

	public DataSourceDescription(
			@JsonProperty("domainIdKey") JsonPointer domainIdKey,
			@JsonProperty("schema") JsonNode schema,
			@JsonProperty("metaData") DataSourceMetaData metaData) {

		super(domainIdKey, schema, metaData);
	}

}
