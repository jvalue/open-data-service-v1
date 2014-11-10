package org.jvalue.ods.main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

import io.dropwizard.Configuration;


public final class OdsConfiguration extends Configuration {

	@NotEmpty
	private final int port;


	@JsonCreator
	public OdsConfiguration(
			@JsonProperty("port") int port) {
		this.port = port;
	}


	public int getPort() {
		return port;
	}

}
