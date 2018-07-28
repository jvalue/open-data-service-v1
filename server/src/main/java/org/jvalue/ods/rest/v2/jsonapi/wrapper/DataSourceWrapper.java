package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;

public class DataSourceWrapper extends DataSource implements JsonApiIdentifiable {


	public DataSourceWrapper(String id, JsonPointer domainIdKey, JsonNode schema, DataSourceMetaData metaData) {
		super(id, domainIdKey, schema, metaData);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getType() {
		return DataSource.class.getSimpleName();
	}
}
