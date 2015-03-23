package org.jvalue.ods.auth;


import com.google.inject.AbstractModule;

import org.jvalue.commons.auth.BasicAuthenticator;
import org.jvalue.commons.auth.BasicCredentials;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthModule extends AbstractModule {

	private final List<BasicCredentials> admins;

	public AuthModule(List<BasicCredentials> admins) {
		this.admins = admins;
	}


	@Override
	protected void configure() {
		Map<BasicCredentials, User> userMap = new HashMap<>();
		for (BasicCredentials credentials : admins) {
			userMap.put(credentials, new User(credentials.getUsername(), Role.ADMIN));
		}
		bind(BasicAuthenticator.class).toInstance(new BasicAuthenticator(userMap));
	}

}
