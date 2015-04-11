package org.jvalue.ods.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.commons.auth.UserDescription;
import org.jvalue.commons.couchdb.CouchDbConfig;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;


public final class OdsConfig extends Configuration {

	@NotNull private final String gcmApiKey;
	@NotNull @Valid private final CouchDbConfig couchDb;
	@NotNull @Valid private final List<UserDescription> admins;

	@JsonCreator
	public OdsConfig(
			@JsonProperty("gcmApiKey") String gcmApiKey,
			@JsonProperty("couchDb") CouchDbConfig couchDb,
			@JsonProperty("admins") List<UserDescription> admins) {

		this.gcmApiKey = gcmApiKey;
		this.couchDb = couchDb;
		this.admins = admins;
	}


	public String getGcmApiKey() {
		return gcmApiKey;
	}


	public CouchDbConfig getCouchDb() {
		return couchDb;
	}


	public List<UserDescription> getAdmins() {
		return admins;
	}

}