package org.jvalue.ods.auth;


/**
 * Returns {@link org.jvalue.ods.auth.User} objects based on {@link org.jvalue.ods.auth.BasicCredentials}.
 */
public final class BasicAuthenticator implements Authenticator<BasicCredentials> {

	@Override
	public User authenticate(BasicCredentials credentials) {
		if (credentials.getUsername().equalsIgnoreCase("admin")
			&& credentials.getPassword().equalsIgnoreCase("admin"))
			return new User();

		return null;
	}

}
