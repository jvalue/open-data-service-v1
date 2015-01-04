package org.jvalue.ods.rest.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import org.jvalue.ods.api.sources.DataSourceMetaData;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DataSource {

	public String id;
	public DataSourceMetaData metaData;
	public String domainIdKey;
	public JsonNode schema;

}
