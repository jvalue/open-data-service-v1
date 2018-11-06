package org.jvalue.ods.rest.v2.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jvalue.commons.auth.AbstractUserDescription;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
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

import static org.jvalue.ods.rest.v2.api.AbstractApi.BASICAUTH;
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


	@Operation(
		operationId = USERS,
		tags = USERS,
		summary = "Get all users",
		description = "Get all users registered at the Open-Data-Service"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.UserSchema.class)),
		links = @Link(name = ENTRYPOINT, operationRef = ENTRYPOINT, description = "Go to api entrypoint")
	)
	@ApiResponse(
		responseCode = "401",
		description = "Not authorized"
	)
	@SecurityRequirement(name = BASICAUTH)
	@GET
	public Response getAllUsers(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true)
			User user) {
		List<User> users = userApiReference.getAllUsers(user);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.fromCollection(users))
			.addLink(ENTRYPOINT, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = USERS,
		summary = "Add user",
		description = "Add a user to the Open-Data-Service"
	)
	@ApiResponse(
		responseCode = "201",
		description = "User added",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.UserSchema.class)),
		links = @Link(name = USERS, operationRef = USERS, description = "Get all users")
	)
	@ApiResponse(
		responseCode = "401",
		description = "Not authorized"
	)
	@SecurityRequirement(name = BASICAUTH)
	@POST
	public Response addUser(
		@RestrictedTo(value = Role.ADMIN, isOptional = true) User user,
		@RequestBody(content = @Content(schema = @Schema(implementation = JsonApiSchema.UserSchema.class)))
			JsonApiRequest userDescriptionRequest) {

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


	@Operation(
		tags = USERS,
		summary = "Get user",
		description = "Get a specific user by its id"
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.UserSchema.class)),
		links = @Link(name = USERS, operationRef = USERS, description = "Get all users")
	)
	@ApiResponse(
		responseCode = "401",
		description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404",
		description = "User not found"
	)
	@GET
	@Path("/{userId}")
	public Response getUser(
		@RestrictedTo(Role.PUBLIC) @Parameter(hidden = true)
			User user,
		@PathParam("userId") @Parameter(description = "Id of the user")
			String userId) {

		User result = userApiReference.getUser(user, userId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.from(result))
			.addLink(USERS, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = USERS,
		summary = "Get yourself",
		description = "Get your own user data"
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.UserSchema.class)),
		links = @Link(name = USERS, operationRef = USERS, description = "Get all users")
	)
	@GET
	@Path("/me")
	public Response getUser(
		@RestrictedTo(Role.PUBLIC) @Parameter(hidden = true)
			User user) {

		User result = userApiReference.getUser(user);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(UserWrapper.from(result))
			.addLink(USERS, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = USERS,
		summary = "Remove user",
		description = "Remove a user from the Open-Data-Service"
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "200", description = "User removed"
	)
	@ApiResponse(
		responseCode = "401",
		description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404", description = "User not found"
	)
	@DELETE
	@Path("/{userId}")
	public void removeUser(
		@RestrictedTo(Role.PUBLIC) @Parameter(hidden = true) User user,
		@PathParam("userId") @Parameter(description = "Id of the user to be deleted") String userId) {

		userApiReference.removeUser(user, userId);
	}
}
