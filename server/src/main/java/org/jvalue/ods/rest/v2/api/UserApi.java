package org.jvalue.ods.rest.v2.api;

import org.jvalue.commons.auth.AbstractUserDescription;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.UserWrapper;
import org.jvalue.ods.utils.JsonMapper;
import org.jvalue.ods.utils.RequestValidator;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static org.jvalue.ods.rest.v2.api.AbstractApi.ENTRYPOINT;
import static org.jvalue.ods.rest.v2.api.AbstractApi.USERS;
import static org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse.JSONAPI_TYPE;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;

/**
 * Empty class which calls the referenced UserApi from jvalue commons for every single method.
 * The former (v1) inheritance hack could not be used because overriding the endpoints with new
 * return types is not possible (yes, it still is unfortunately a hack ...).
 */
@Path(AbstractApi.V2 + "/" + USERS)
@Produces(JSONAPI_TYPE)
public class UserApi {

	private final org.jvalue.commons.auth.rest.UserApi userApiReference;

	@Context
	private UriInfo uriInfo;

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
			.addLink(ENTRYPOINT, getDirectoryURI(uriInfo))
			.build();
	}


	@POST
	public Response addUser(@RestrictedTo(value = Role.ADMIN, isOptional = true) User user, JsonApiRequest userDescriptionRequest) {

		AbstractUserDescription userDescription = JsonMapper.convertValue(
			userDescriptionRequest.getAttributes(),
			AbstractUserDescription.class
		);

		RequestValidator.validate(userDescription);

		User result = userApiReference.addUser(user, userDescription);

		URI directoryURI = getSanitizedPath(uriInfo);

		return JsonApiResponse
			.createPostResponse(uriInfo)
			.data(UserWrapper.from(result))
			.addLink(JsonLinks.SELF, directoryURI.resolve(userDescriptionRequest.getId()))
			.addLink(USERS, directoryURI)
			.build();
	}


	@GET
	@Path("/{userId}")
	public Response getUser(@RestrictedTo(Role.PUBLIC) User user, @PathParam("userId") String userId) {
		User result = userApiReference.getUser(user, userId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.from(result))
			.addLink(USERS, getDirectoryURI(uriInfo))
			.build();
	}


	@GET
	@Path("/me")
	public Response getUser(@RestrictedTo(Role.PUBLIC) User user) {
		User result = userApiReference.getUser(user);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.from(result))
			.addLink(USERS, getDirectoryURI(uriInfo))
			.build();
	}


	@DELETE
	@Path("/{userId}")
	public void removeUser(@RestrictedTo(Role.PUBLIC) User user, @PathParam("userId") String userId) {
		userApiReference.removeUser(user, userId);
	}
}
