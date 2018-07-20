package org.jvalue.ods.api.views.couchdb;


import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;


public abstract class AbstractCouchDbDataView {

	@NotNull private final String mapFunction;
	private final String reduceFunction;

	/**
	 * Create a new map reduce view on the data.
	 * @param mapFunction the map function written in JavaScript
	 * @param reduceFunction the optional reduce function written in JavaScript
	 */
	protected AbstractCouchDbDataView(
			String mapFunction,
			String reduceFunction) {

		this.mapFunction = mapFunction;
		this.reduceFunction = reduceFunction;
	}


	public String getMapFunction() {
		return mapFunction;
	}


	public String getReduceFunction() {
		return reduceFunction;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AbstractCouchDbDataView)) return false;
		if (other == this) return true;
		AbstractCouchDbDataView view = (AbstractCouchDbDataView) other;
		return Objects.equal(mapFunction, view.mapFunction)
				&& Objects.equal(reduceFunction, view.reduceFunction);

	}


	@Override
	public int hashCode() {
		return Objects.hashCode(mapFunction, reduceFunction);
	}

}
