package org.jvalue.ods.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;


public final class OdsConfig extends Configuration {

	@NotNull private final String gcmApiKey;
	@NotNull private final long grabberUpdateInterval; // measured in seconds

	@JsonCreator
	public OdsConfig(
			@JsonProperty("gcmApiKey") String gcmApiKey,
			@JsonProperty("grabberUpdateInterval") long grabberUpdateInterval) {

		this.gcmApiKey = gcmApiKey;
		this.grabberUpdateInterval = grabberUpdateInterval;
	}


	public String getGcmApiKey() {
		return gcmApiKey;
	}


	public long getGrabberUpdateInterval() {
		return grabberUpdateInterval;
	}

}