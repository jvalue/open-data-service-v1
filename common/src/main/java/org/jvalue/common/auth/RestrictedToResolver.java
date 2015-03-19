package org.jvalue.common.auth;


import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;


/**
 * Determines where the {@link org.jvalue.common.auth.RestrictedTo} annotation can
 * appear (on parameters).
 */
public final class RestrictedToResolver extends ParamInjectionResolver<RestrictedTo> {

	public RestrictedToResolver() {
		super(RestrictedToProvider.class);
	}

}
