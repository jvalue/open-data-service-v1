package org.jvalue.ods.filter.reference;


import java.util.EnumSet;
import java.util.Set;

public enum FilterType {

	OUTPUT_FILTER,
	INPUT_OUTPUT_FILTER,
	INPUT_FILTER;

	static {
		OUTPUT_FILTER.nextFilterTypes = EnumSet.of(INPUT_OUTPUT_FILTER, INPUT_FILTER);
		INPUT_OUTPUT_FILTER.nextFilterTypes = EnumSet.of(INPUT_OUTPUT_FILTER, INPUT_FILTER);
		INPUT_FILTER.nextFilterTypes = EnumSet.noneOf(FilterType.class);
	}

	private Set<FilterType> nextFilterTypes;

	public boolean isValidNextFilter(FilterType filterType) {
		return nextFilterTypes.contains(filterType);
	}

}
