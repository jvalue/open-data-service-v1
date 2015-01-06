package org.jvalue.ods.api.views;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DataViewDescription extends AbstractDataView {

	@JsonCreator
	public DataViewDescription(
			@JsonProperty("mapFunction") String mapFunction,
			@JsonProperty("reduceFunction") String reduceFunction) {

		super(mapFunction, reduceFunction);
	}


	public DataViewDescription(String mapFunction) {
		this(mapFunction, null);
	}

}
