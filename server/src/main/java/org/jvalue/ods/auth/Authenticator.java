package org.jvalue.ods.auth;


import com.google.common.base.Optional;

/**
 * Tries to match {@link org.jvalue.ods.auth.User} objects to credentials.
 *
 * @param <T> the credentials
 */
public interface Authenticator<T> {

	/**
	 * @return the authenticated user if any.
	 */
	public Optional<User> authenticate(T credentials, Role requiredRole);

}
