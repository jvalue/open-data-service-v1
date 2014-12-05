package org.jvalue.ods.filter.reference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.utils.Assert;

import java.util.List;


public final class FilterChainReference extends CouchDbDocument {

	private final String filterChainId;
	private final List<FilterReference> filters;
	private final FilterChainExecutionInterval executionInterval;

	@JsonCreator
	FilterChainReference(
			@JsonProperty("filterChainId") String filterChainId,
			@JsonProperty("filters") List<FilterReference> filters,
			@JsonProperty("executionInterval") FilterChainExecutionInterval executionInterval) {

		Assert.assertNotNull(filterChainId, filters, executionInterval);
		this.filterChainId = filterChainId;
		this.filters = filters;
		this.executionInterval = executionInterval;
	}


	public String getFilterChainId() {
		return filterChainId;
	}


	public List<FilterReference> getFilters() {
		return filters;
	}


	public FilterChainExecutionInterval getExecutionInterval() {
		return executionInterval;
	}

}
