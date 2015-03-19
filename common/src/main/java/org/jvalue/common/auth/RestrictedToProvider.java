package org.jvalue.common.auth;


import com.google.common.base.Optional;
import com.google.common.io.BaseEncoding;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;


/**
 * Responsible for extracting credentials from an http request and forwarding
 * those to an {@link org.jvalue.common.auth.Authenticator}.
 */
public final class RestrictedToProvider extends AbstractValueFactoryProvider {

	private final BasicAuthenticator basicAuthenticator;

	@Inject
	protected RestrictedToProvider(
			MultivaluedParameterExtractorProvider mpep,
			ServiceLocator locator,
			BasicAuthenticator basicAuthenticator) {

		super(mpep, locator, Parameter.Source.UNKNOWN);
		this.basicAuthenticator = basicAuthenticator;
	}


	private void onUnauthorized() {
		throw new UnauthorizedException();
	}


	@Override
	protected Factory<User> createValueFactory(final Parameter parameter) {
		Class<?> classType = parameter.getRawType();
		if (classType == null || (!classType.equals(User.class))) return null;

		// factory for getting user instances based on an http request
		return new AbstractContainerRequestValueFactory<User>() {
			@Override
			public User provide() {
				// find auth header
				List<String> headers = getContainerRequest().getRequestHeader(HttpHeaders.AUTHORIZATION);
				if (headers == null || headers.isEmpty()) onUnauthorized();
				String authValue = headers.get(0);

				// check if auth header starts with "Basic "
				int space = authValue.indexOf(' ');
				if (space < 0) onUnauthorized();
				String authType = authValue.substring(0, space);
				if (!"basic".equalsIgnoreCase(authType)) onUnauthorized();

				// decode credentials
				String token = new String(BaseEncoding.base64().decode(
						authValue.substring(space + 1)),
						StandardCharsets.UTF_8);

				int colon = token.indexOf(':');
				if (colon < 0) onUnauthorized();
				String username = token.substring(0, colon);
				String password = token.substring(colon + 1);
				BasicCredentials credentials = new BasicCredentials(username, password);

				// authenticate
				Optional<User> user = basicAuthenticator.authenticate(credentials, parameter.getAnnotation(RestrictedTo.class).value());
				if (!user.isPresent()) onUnauthorized();
				return user.get();
			}
		};
	}
}
