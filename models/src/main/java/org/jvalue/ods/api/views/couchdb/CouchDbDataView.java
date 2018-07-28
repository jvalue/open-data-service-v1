package org.jvalue.ods.api.views.couchdb;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import org.jvalue.commons.EntityBase;

import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CouchDbDataView extends AbstractCouchDbDataView implements EntityBase {

	@SerializedName("_id")
	@NotNull private final String id;

	/**
	 * Create a new map reduce view on the data.
	 * @param id the id of the view
	 * @param mapFunction the map function written in JavaScript
	 * @param reduceFunction the optional reduce function written in JavaScript
	 */
	@JsonCreator
	public CouchDbDataView(
			@JsonProperty("id") String id,
			@JsonProperty("mapFunction") String mapFunction,
			@JsonProperty("reduceFunction") String reduceFunction) {

		super(mapFunction, reduceFunction);
		this.id = id;
	}


	public CouchDbDataView(String id, String mapFunction) {
		this(id, mapFunction, null);
	}

	public String getId() {
		return id;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (!(other instanceof CouchDbDataView)) return false;
		CouchDbDataView view = (CouchDbDataView) other;
		return Objects.equal(id, view.id);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, super.hashCode());
	}

}
