package org.jvalue.common.auth;


import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;

/**
 * Returns {@link User} objects based on {@link BasicCredentials}.
 */
public final class BasicAuthenticator implements Authenticator<BasicCredentials> {

	private final Map<BasicCredentials, User> userMap = new HashMap<>();

	public BasicAuthenticator(Map<BasicCredentials, User> userMap) {
		this.userMap.putAll(userMap);
	}


	@Override
	public Optional<User> authenticate(BasicCredentials credentials, Role requiredRole) {
		User user = userMap.get(credentials);
		if (user == null || !user.getRole().equals(requiredRole)) return Optional.absent();
		return Optional.of(user);
	}

}
