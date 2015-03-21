package org.jvalue.common.auth;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;


/**
 * Set of basic auth credentials (username + password).
 */
public final class BasicCredentials {

	@NotNull private final String username;
	@NotNull private final String password;


	@JsonCreator
	public BasicCredentials(
			@JsonProperty("username") String username,
			@JsonProperty("password") String password) {

		this.username = username;
		this.password = password;
	}


	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof BasicCredentials)) return false;
		BasicCredentials credentials = (BasicCredentials) other;
		return Objects.equal(username, credentials.username)
				&& Objects.equal(password, credentials.password);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(username, password);
	}

}
