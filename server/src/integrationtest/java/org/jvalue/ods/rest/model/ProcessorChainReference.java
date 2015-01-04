package org.jvalue.ods.rest.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.jvalue.ods.api.processors.ExecutionInterval;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessorChainReference {

	public String id;
	public List<Processor> processors;
	public ExecutionInterval executionInterval;

}
