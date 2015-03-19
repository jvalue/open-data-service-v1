package org.jvalue.ods.auth;


import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;


/**
 * Set of basic auth credentials (username + password).
 */
public final class BasicCredentials {

	private final @NotNull String username;
	private final @NotNull String password;


	public BasicCredentials(String username, String password) {
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
