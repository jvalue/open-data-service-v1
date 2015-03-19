package org.jvalue.common.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.validation.constraints.NotNull;


public final class AuthConfig {
	@NotNull private final List<BasicCredentials> admins;

	@JsonCreator
	public AuthConfig(
			@JsonProperty("admins") List<BasicCredentials> admins) {

		this.admins = admins;
	}


	public List<BasicCredentials> getAdmins() {
		return admins;
	}

}