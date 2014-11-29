package org.jvalue.ods.filter.reference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.utils.Assert;

import java.util.List;


public final class FilterChainReference extends CouchDbDocument {

	private final String filterChainId;
	private final List<FilterReference> filterReferences;
	private final FilterChainMetaData metaData;
	private final String dataSourceId; // required to link chains and sources

	@JsonCreator
	FilterChainReference(
			@JsonProperty("filterChainId") String filterChainId,
			@JsonProperty("filterReferences") List<FilterReference> filterReferences,
			@JsonProperty("metaData") FilterChainMetaData metaData,
			@JsonProperty("dataSourceId") String dataSourceId) {

		Assert.assertNotNull(filterChainId, dataSourceId, filterReferences, metaData);
		this.filterChainId = filterChainId;
		this.filterReferences = filterReferences;
		this.metaData = metaData;
		this.dataSourceId = dataSourceId;
	}


	public String getFilterChainId() {
		return filterChainId;
	}


	public List<FilterReference> getFilterReferences() {
		return filterReferences;
	}


	public FilterChainMetaData getMetaData() {
		return metaData;
	}


	public String getDataSourceId() {
		return dataSourceId;
	}

}
