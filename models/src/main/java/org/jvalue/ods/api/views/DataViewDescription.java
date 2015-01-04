package org.jvalue.ods.api.views;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataViewDescription {

	private final String mapFunction, reduceFunction;

	/**
	 * Create a new map reduce view on the data.
	 * @param mapFunction the map function written in JavaScript
	 * @param reduceFunction the optional reduce function written in JavaScript
	 */
	@JsonCreator
	public DataViewDescription(
			@JsonProperty("mapFunction") String mapFunction,
			@JsonProperty("reduceFunction") String reduceFunction) {

		this.mapFunction = mapFunction;
		this.reduceFunction = reduceFunction;
	}


	public DataViewDescription(String mapFunction) {
		this(mapFunction, null);
	}


	public String getMapFunction() {
		return mapFunction;
	}


	public String getReduceFunction() {
		return reduceFunction;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataViewDescription)) return false;
		if (other == this) return true;
		DataViewDescription view = (DataViewDescription) other;
		return Objects.equal(mapFunction, view.mapFunction)
				&& Objects.equal(reduceFunction, view.reduceFunction);

	}


	@Override
	public int hashCode() {
		return Objects.hashCode(mapFunction, reduceFunction);
	}

}
