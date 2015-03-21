package org.jvalue.common.auth;


import com.google.common.base.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BasicAuthenticatorTest {

	private final User publicUser = new User("bob", Role.PUBLIC);
	private final BasicCredentials publicCredentials = new BasicCredentials("user", "pass");

	private BasicAuthenticator authenticator;

	@Before
	public void setup() {
		Map<BasicCredentials, User> userMap = new HashMap<>();
		userMap.put(publicCredentials, publicUser);
		authenticator = new BasicAuthenticator(userMap);
	}

	@Test
	public void testValidUser() {
		Optional<User> user = authenticator.authenticate(publicCredentials, Role.PUBLIC);
		Assert.assertEquals(publicUser, user.get());
	}


	@Test
	public void testInvalidCredentials() {
		BasicCredentials credentials = new BasicCredentials("foo", "bar");
		Optional<User> user = authenticator.authenticate(credentials, Role.PUBLIC);
		Assert.assertFalse(user.isPresent());
	}


	@Test
	public void testInvalidRole() {
		Optional<User> user = authenticator.authenticate(publicCredentials, Role.ADMIN);
		Assert.assertFalse(user.isPresent());
	}

}
