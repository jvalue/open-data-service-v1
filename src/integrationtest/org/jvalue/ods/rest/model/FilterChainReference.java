package org.jvalue.ods.rest.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.jvalue.ods.filter.reference.FilterChainExecutionInterval;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterChainReference {

	public String id;
	public List<FilterReference> filterReferences;
	public List<String> filterNames;
	public FilterChainExecutionInterval executionInterval;

}
