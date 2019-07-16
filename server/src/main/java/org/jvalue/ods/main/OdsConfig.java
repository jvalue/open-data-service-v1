/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.jvalue.commons.auth.AuthConfig;
import org.jvalue.commons.couchdb.CouchDbConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public final class OdsConfig extends Configuration {

	@NotNull private final String gcmApiKey;
	@NotNull @Valid private final CouchDbConfig couchDb;
	@NotNull @Valid private final AuthConfig auth;

	@JsonCreator
	public OdsConfig(
			@JsonProperty("gcmApiKey") String gcmApiKey,
			@JsonProperty("couchDb") CouchDbConfig couchDb,
			@JsonProperty("auth") AuthConfig auth) {

		this.gcmApiKey = gcmApiKey;
		this.couchDb = couchDb;
		this.auth = auth;
	}


	public String getGcmApiKey() {
		return gcmApiKey;
	}


	public CouchDbConfig getCouchDb() {
		return couchDb;
	}


	public AuthConfig getAuth() {
		return auth;
	}

}
