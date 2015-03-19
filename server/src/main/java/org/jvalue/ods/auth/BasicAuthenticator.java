package org.jvalue.ods.auth;


import com.google.common.base.Optional;

/**
 * Returns {@link org.jvalue.ods.auth.User} objects based on {@link org.jvalue.ods.auth.BasicCredentials}.
 */
public final class BasicAuthenticator implements Authenticator<BasicCredentials> {

	@Override
	public Optional<User> authenticate(BasicCredentials credentials, Role requiredRole) {
		if (credentials.getUsername().equalsIgnoreCase("admin")
				&& credentials.getPassword().equalsIgnoreCase("admin")) {
			Optional<User> user = Optional.of(new User("admin", Role.ADMIN));
			if (user.get().getRole().equals(requiredRole)) return user;
		}

		return Optional.absent();
	}

}
