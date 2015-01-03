/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.utils.Assert;


@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DataView extends CouchDbDocument {

	private final String viewId;
	private final String mapFunction, reduceFunction;

	/**
	 * Create a new map reduce view on the data.
	 * @param viewId the id of the view
	 * @param mapFunction the map function writen in JavaScript
	 * @param reduceFunction the optional reduce function writen in JavaScript
	 */
	@JsonCreator
	public DataView(
			@JsonProperty("viewId") String viewId,
			@JsonProperty("mapFunction") String mapFunction,
			@JsonProperty("reduceFunction") String reduceFunction) {

		Assert.assertNotNull(viewId, mapFunction);
		this.viewId = viewId;
		this.mapFunction = mapFunction;
		this.reduceFunction = reduceFunction;
	}


	public DataView(String viewId, String mapFunction) {
		this(viewId, mapFunction, null);
	}


	public String getViewId() {
		return viewId;
	}


	public String getMapFunction() {
		return mapFunction;
	}


	public String getReduceFunction() {
		return reduceFunction;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataView)) return false;
		if (other == this) return true;
		DataView view = (DataView) other;
		return Objects.equal(viewId, view.viewId)
				&& Objects.equal(mapFunction, view.mapFunction)
				&& Objects.equal(reduceFunction, view.reduceFunction);

	}


	@Override
	public int hashCode() {
		return Objects.hashCode(viewId, mapFunction, reduceFunction);
	}

}
