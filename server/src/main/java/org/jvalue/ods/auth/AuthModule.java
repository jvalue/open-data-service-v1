package org.jvalue.ods.auth;

import com.google.inject.AbstractModule;

import org.jvalue.commons.auth.AuthConfig;

public final class AuthModule extends AbstractModule {

	private final AuthConfig authConfig;

	public AuthModule(AuthConfig authConfig) {
		this.authConfig = authConfig;
	}

	@Override
	protected void configure() {
		bind(AuthConfig.class).toInstance(authConfig);
	}

}
