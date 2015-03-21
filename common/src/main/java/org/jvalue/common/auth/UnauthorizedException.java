package org.jvalue.common.auth;


/**
 * Nope, you didn't have those rights ...
 */
public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {
		super(message);
	}


	public UnauthorizedException() { }

}
