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

	@JsonCreator
	FilterChainReference(
			@JsonProperty("filterChainId") String filterChainId,
			@JsonProperty("filterReferences") List<FilterReference> filterReferences,
			@JsonProperty("metaData") FilterChainMetaData metaData) {

		Assert.assertNotNull(filterChainId, filterReferences, metaData);
		this.filterChainId = filterChainId;
		this.filterReferences = filterReferences;
		this.metaData = metaData;
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

}
