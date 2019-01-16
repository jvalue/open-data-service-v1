/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.util.Collection;
import java.util.stream.Collectors;

@Schema(name = "dataSourceData")
public class DataSourceWrapper implements JsonApiIdentifiable {

	private final DataSource dataSource;

	private DataSourceWrapper(String id, JsonPointer domainIdKey, JsonNode schema, DataSourceMetaData metaData) {
		this.dataSource = new DataSource(id, domainIdKey, schema, metaData);
	}


	@Schema(name = "attributes", required = true)
	@JsonIgnoreProperties(value = "id")
	@JsonUnwrapped
	public DataSource getDataSource() {
		return dataSource;
	}


	@Schema(example = "pegelonline", required = true)
	@Override
	public String getId() {
		return dataSource.getId();
	}


	@Schema(allowableValues = "DataSource", required = true)
	@Override
	public String getType() {
		return DataSource.class.getSimpleName();
	}


	/**
	 * Wrap a single DataSource
	 * @param dataSource the source to wrap
	 * @return a DataSourceWrapper which wraps dataSource
	 */
	public static DataSourceWrapper from(DataSource dataSource) {
		return new DataSourceWrapper(
			dataSource.getId(),
			dataSource.getDomainIdKey(),
			dataSource.getSchema(),
			dataSource.getMetaData());
	}


	/**
	 * Wrap a collection of DataSources
	 * @param sourcesCollection the collection to wrap
	 * @return a collection of DataSourcesWrappers wrapping the elements of sourcesCollection
	 */
	public static Collection<DataSourceWrapper> fromCollection(Collection<DataSource> sourcesCollection) {
		return sourcesCollection
				.stream()
				.map(DataSourceWrapper::from)
				.collect(Collectors.toList());
	}
}
