package org.jvalue.common.auth;


import com.google.common.base.Optional;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;

import javax.inject.Provider;
import javax.ws.rs.core.HttpHeaders;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class RestrictedToProviderTest {

	@Mocked private MultivaluedParameterExtractorProvider mpep;
	@Mocked private ServiceLocator serviceLocator;

	@Mocked private Parameter parameter;
	@Mocked private Provider<ContainerRequest> requestProvider;
	@Mocked private ContainerRequest containerRequest;

	@Mocked BasicAuthenticator basicAuthenticator;

	private RestrictedToProvider provider;

	private final User adminUser = new User("admin", Role.ADMIN);
	private final BasicCredentials adminCredentials = new BasicCredentials("admin", "admin");
	private final String adminHeader = "Basic YWRtaW46YWRtaW4="; // admin:admin


	@Before
	public void setup() {
		provider = new RestrictedToProvider(mpep, serviceLocator, basicAuthenticator);
	}


	@Test
	public void testValidAuth() {
		Factory<User> factory = setupMocks(adminHeader, adminUser, Role.ADMIN);
		User user = factory.provide();
		Assert.assertEquals(adminUser, user);
	}


	@Test(expected = UnauthorizedException.class)
	public void testMissingHeader() {
		Factory<User> factory = setupMocks(null, adminUser, Role.ADMIN);
		factory.provide();
	}


	@Test(expected = UnauthorizedException.class)
	public void testInvalidCredentials() {
		Factory<User> factory = setupMocks("Basic Zm9vOmJhcg==", adminUser, Role.ADMIN);
		factory.provide();
	}


	@Test(expected = UnauthorizedException.class)
	public void testInvalidRole() {
		Factory<User> factory = setupMocks(adminHeader, adminUser, Role.PUBLIC);
		factory.provide();
	}


	private Factory<User> setupMocks(
			final String authHeader,
			final User user,
			final Role userRole) {

		new Expectations() {{
			basicAuthenticator.authenticate(adminCredentials, userRole);
			result = Optional.of(user);
			minTimes = 0;

			parameter.getRawType(); result = User.class;
			parameter.getAnnotation(RestrictedTo.class); result = new RestrictedTo() {
				@Override
				public Class<? extends Annotation> annotationType() { return null; }

				@Override
				public Role value() {
					return Role.ADMIN;
				}
			};
			minTimes = 0;

			requestProvider.get(); result = containerRequest;

			containerRequest.getRequestHeader(HttpHeaders.AUTHORIZATION);
			result = authHeader;
		}};

		Factory<User> factory = provider.createValueFactory(parameter);
		Deencapsulation.setField(factory, requestProvider);
		return factory;
	}

}
