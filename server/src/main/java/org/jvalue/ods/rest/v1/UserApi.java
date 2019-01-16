/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v1;

import org.jvalue.commons.auth.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Empty class which calls super for every single method. Its solve purpose is to allow JaxRs2Retrofit to
 * create client code (yes, it's unfortunately a hack ...).
 */
@Path(AbstractApi.VERSION + "/users")
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

