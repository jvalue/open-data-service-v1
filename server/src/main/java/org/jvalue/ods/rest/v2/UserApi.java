package org.jvalue.ods.rest.v2;

import org.jvalue.commons.auth.AbstractUserDescription;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiMediaType;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.UserWrapper;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Empty class which calls the referenced UserApi from jvalue commons for every single method.
 * The former (v1) inheritance hack could not be used because overriding the endpoints with new
 * return types is not possible (yes, it still is unfortunately a hack ...).
 */
@Path(AbstractApi.VERSION + "/users")
@Produces(JsonApiMediaType.JSONAPI)
public class UserApi {

	private final org.jvalue.commons.auth.rest.UserApi userApiReference;

	@Context private UriInfo uriInfo;

	@Inject
	public UserApi(org.jvalue.commons.auth.rest.UserApi userApi) {
		this.userApiReference = userApi;
	}


	@GET
	public Response getAllUsers(@RestrictedTo(Role.ADMIN) User user) {
		List<User> users = userApiReference.getAllUsers(user);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.fromCollection(users))
			.build();
	}


	@POST
	public Response addUser(@RestrictedTo(value = Role.ADMIN, isOptional = true) User user, AbstractUserDescription userDescription) {
		User result = userApiReference.addUser(user, userDescription);

		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(UserWrapper.from(result))
			.build();
	}


	@GET
	@Path("/{userId}")
	public Response getUser(@RestrictedTo(Role.PUBLIC) User user, @PathParam("userId") String userId) {
		User result = userApiReference.getUser(user, userId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.from(result))
			.build();
	}


	@GET
	@Path("/me")
	public Response getUser(@RestrictedTo(Role.PUBLIC) User user) {
		User result = userApiReference.getUser(user);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.from(result))
			.build();
	}


	@DELETE
	@Path("/{userId}")
	public void removeUser(@RestrictedTo(Role.PUBLIC) User user, @PathParam("userId") String userId) {
		userApiReference.removeUser(user, userId);
	}
}
