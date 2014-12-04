package org.jvalue.ods.rest.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.jvalue.ods.filter.reference.FilterType;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class FilterReference {

	public String filterName;
	public FilterType filterType;

}
