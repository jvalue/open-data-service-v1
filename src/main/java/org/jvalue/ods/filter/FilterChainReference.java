package org.jvalue.ods.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.ods.utils.Assert;

import java.util.List;

public final class FilterChainReference {

	private final List<FilterReference> filterReferences;
	private final FilterChainMetaData metaData;

	@JsonCreator
	public FilterChainReference(
			@JsonProperty("filterReferences") List<FilterReference> filterReferences,
			@JsonProperty("metaData") FilterChainMetaData metaData) {

		Assert.assertNotNull(filterReferences, metaData);
		this.filterReferences = filterReferences;
		this.metaData = metaData;
	}


	public List<FilterReference> getFilterReferences() {
		return filterReferences;
	}


	public FilterChainMetaData getMetaData() {
		return metaData;
	}

}
