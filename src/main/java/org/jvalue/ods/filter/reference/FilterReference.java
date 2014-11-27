package org.jvalue.ods.filter.reference;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;

public final class FilterReference {

	private final String filterName;
	private final FilterType filterType;

	@JsonCreator
	FilterReference(
		 	@JsonProperty("filterName") String filterName,
			@JsonProperty("filterType") FilterType filterType) {

		Assert.assertNotNull(filterName, filterType);
		this.filterName = filterName;
		this.filterType = filterType;
	}


	public String getFilterName() {
		return filterName;
	}


	public FilterType getFilterType() {
		return filterType;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof FilterReference)) return false;
		if (other == this) return true;
		FilterReference reference = (FilterReference) other;
		return Objects.equal(filterName, reference.filterName)
				&& Objects.equal(filterType, reference.filterType);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(filterName, filterType);
	}

}
