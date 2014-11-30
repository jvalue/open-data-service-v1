package org.jvalue.ods.filter.reference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.utils.Assert;

import java.util.List;


public final class FilterChainReference extends CouchDbDocument {

	private final String filterChainId;
	private final List<FilterReference> filterReferences;
	private final FilterChainExecutionInterval executionInterval;

	@JsonCreator
	FilterChainReference(
			@JsonProperty("filterChainId") String filterChainId,
			@JsonProperty("filterReferences") List<FilterReference> filterReferences,
			@JsonProperty("executionInterval") FilterChainExecutionInterval executionInterval) {

		Assert.assertNotNull(filterChainId, filterReferences, executionInterval);
		this.filterChainId = filterChainId;
		this.filterReferences = filterReferences;
		this.executionInterval = executionInterval;
	}


	public String getFilterChainId() {
		return filterChainId;
	}


	public List<FilterReference> getFilterReferences() {
		return filterReferences;
	}


	public FilterChainExecutionInterval getExecutionInterval() {
		return executionInterval;
	}

}
