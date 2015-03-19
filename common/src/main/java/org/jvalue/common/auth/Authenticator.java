package org.jvalue.common.auth;


import com.google.common.base.Optional;

import org.jvalue.ods.api.auth.Role;
import org.jvalue.ods.api.auth.User;

/**
 * Tries to match {@link User} objects to credentials.
 *
 * @param <T> the credentials
 */
public interface Authenticator<T> {

	/**
	 * @return the authenticated user if any.
	 */
	public Optional<User> authenticate(T credentials, Role requiredRole);

}
