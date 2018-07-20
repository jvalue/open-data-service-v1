package org.jvalue.ods.api.views.couchdb;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CouchDbDataViewDescription extends AbstractCouchDbDataView {

	@JsonCreator
	public CouchDbDataViewDescription(
			@JsonProperty("mapFunction") String mapFunction,
			@JsonProperty("reduceFunction") String reduceFunction) {

		super(mapFunction, reduceFunction);
	}


	public CouchDbDataViewDescription(String mapFunction) {
		this(mapFunction, null);
	}

}
