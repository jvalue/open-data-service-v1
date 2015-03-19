package org.jvalue.common.auth;


import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Dependency injection binder for jersey authentication
 */
public final class AuthBinder extends AbstractBinder {

	private final BasicAuthenticator basicAuthenticator;

	@Inject
	AuthBinder(BasicAuthenticator basicAuthenticator) {
		this.basicAuthenticator = basicAuthenticator;
	}


	@Override
	protected void configure() {
		bind(basicAuthenticator);

		bind(RestrictedToProvider.class)
				.to(ValueFactoryProvider.class)
				.in(Singleton.class);

		bind(RestrictedToResolver.class)
				.to(new TypeLiteral<InjectionResolver<RestrictedTo>>() { })
				.in(Singleton.class);
	}

}
