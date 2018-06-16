package org.jvalue.ods.rest.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class AbstractApi {

    @JsonProperty
	protected static final String BASE_URL = "/v1/datasources";
}
