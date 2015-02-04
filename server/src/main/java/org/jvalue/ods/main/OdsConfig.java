package org.jvalue.ods.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.common.db.CouchDbConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;


public final class OdsConfig extends Configuration {

	@NotNull private final String gcmApiKey;
	@NotNull @Valid private final CouchDbConfig couchDb;

	@JsonCreator
	public OdsConfig(
			@JsonProperty("gcmApiKey") String gcmApiKey,
			@JsonProperty("couchDb") CouchDbConfig couchDb) {

		this.gcmApiKey = gcmApiKey;
		this.couchDb = couchDb;
	}


	public String getGcmApiKey() {
		return gcmApiKey;
	}


	public CouchDbConfig getCouchDb() {
		return couchDb;
	}

}