package org.jvalue.ods.filter;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.utils.Assert;

public final class FilterReference {

	private final String filterKey;

	@JsonCreator
	public FilterReference(
		 	@JsonProperty("filterKey") String filterKey) {

		Assert.assertNotNull(filterKey);
		this.filterKey = filterKey;
	}


	public String getFilterKey() {
		return filterKey;
	}

}
