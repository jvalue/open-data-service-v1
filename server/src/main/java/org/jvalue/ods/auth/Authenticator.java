package org.jvalue.ods.auth;


/**
 * Tries to match {@link org.jvalue.ods.auth.User} objects to credentials.
 *
 * @param <T> the credentials
 */
public interface Authenticator<T> {

	/**
	 * @return null if unable to validate the credentials..
	 */
	public User authenticate(T credentials);

}
