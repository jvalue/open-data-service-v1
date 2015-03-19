package org.jvalue.ods.auth;


import com.google.inject.AbstractModule;

import org.jvalue.common.auth.AuthConfig;
import org.jvalue.common.auth.BasicAuthenticator;
import org.jvalue.common.auth.BasicCredentials;
import org.jvalue.common.auth.Role;
import org.jvalue.common.auth.User;

import java.util.HashMap;
import java.util.Map;

public class AuthModule extends AbstractModule {

	private final AuthConfig authConfig;

	public AuthModule(AuthConfig authConfig) {
		this.authConfig = authConfig;
	}


	@Override
	protected void configure() {
		Map<BasicCredentials, User> userMap = new HashMap<>();
		for (BasicCredentials credentials : authConfig.getAdmins()) {
			userMap.put(credentials, new User(credentials.getUsername(), Role.ADMIN));
		}
		bind(BasicAuthenticator.class).toInstance(new BasicAuthenticator(userMap));
	}

}
