package org.jvalue.ods.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;


public final class OdsConfig extends Configuration {

	@NotNull private final String gcmApiKey;
	@NotNull private final String couchdbUrl;

	@JsonCreator
	public OdsConfig(
			@JsonProperty("gcmApiKey") String gcmApiKey,
			@JsonProperty("couchDbUrl") String couchdbUrl) {

		this.gcmApiKey = gcmApiKey;
		this.couchdbUrl = couchdbUrl;
	}


	public String getGcmApiKey() {
		return gcmApiKey;
	}


	public String getCouchdbUrl() {
		return couchdbUrl;
	}

}