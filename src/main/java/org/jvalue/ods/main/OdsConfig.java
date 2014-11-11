package org.jvalue.ods.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;


public final class OdsConfig extends Configuration {

	@NotNull
	private final String gcmApiKey;

	@JsonCreator
	public OdsConfig(
			@JsonProperty("gcmApiKey") String gcmApiKey) {
		this.gcmApiKey = gcmApiKey;
	}

	public String getGcmApiKey() {
		return gcmApiKey;
	}

}