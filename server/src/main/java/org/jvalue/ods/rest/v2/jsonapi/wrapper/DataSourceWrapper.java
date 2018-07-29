package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.util.Collection;
import java.util.stream.Collectors;

public class DataSourceWrapper extends DataSource implements JsonApiIdentifiable {


	private DataSourceWrapper(String id, JsonPointer domainIdKey, JsonNode schema, DataSourceMetaData metaData) {
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


	/**
	 * Wrap a single DataSource
	 * @param dataSource the source to wrap
	 * @return a DataSourceWrapper which wraps {@param dataSource}
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
	 * @return a collection of DataSourcesWrappers wrapping the elements of {@param sourcesCollection}
	 */
	public static Collection<DataSourceWrapper> fromCollection(Collection<DataSource> sourcesCollection) {
		return sourcesCollection
				.stream()
				.map(DataSourceWrapper::from)
				.collect(Collectors.toList());
	}
}
