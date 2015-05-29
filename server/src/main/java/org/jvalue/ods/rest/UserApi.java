package org.jvalue.ods.rest;

import org.jvalue.commons.auth.AbstractUserDescription;
import org.jvalue.commons.auth.BasicAuthUtils;
import org.jvalue.commons.auth.BasicAuthenticator;
import org.jvalue.commons.auth.OAuthUtils;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.auth.UserManager;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Empty class which calls super for every single method. Its solve purpose is to allow JaxRs2Retrofit to
 * create client code (yes, it's unfortunately a hack ...).
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class UserApi extends org.jvalue.commons.auth.rest.UserApi {

	@Inject
	UserApi(UserManager userManager, BasicAuthenticator basicAuthenticator, BasicAuthUtils basicAuthUtils, OAuthUtils oAuthUtils) {
		super(userManager, basicAuthenticator, basicAuthUtils, oAuthUtils);
	}


	@Override
	@GET
	public List<User> getAllUsers(@RestrictedTo(Role.ADMIN) User user) {
		return super.getAllUsers(user);
	}


	@Override
	@POST
	public User addUser(@RestrictedTo(value = Role.ADMIN, isOptional = true) User user, AbstractUserDescription userDescription) {
		return super.addUser(user, userDescription);
	}


	@Override
	@GET
	@Path("/{userId}")
	public User getUser(@RestrictedTo(Role.PUBLIC) User user, @PathParam("userId") String userId) {
		return super.getUser(user, userId);
	}


	@Override
	@GET
	@Path("/me")
	public User getUser(@RestrictedTo(Role.PUBLIC) User user) {
		return super.getUser(user);
	}


	@Override
	@DELETE
	@Path("/{userId}")
	public void removeUser(@RestrictedTo(Role.PUBLIC) User user, @PathParam("userId") String userId) {
		super.removeUser(user, userId);
	}

}

