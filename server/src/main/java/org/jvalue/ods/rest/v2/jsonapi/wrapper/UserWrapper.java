package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.rest.v2.api.UserApi;

import java.util.Collection;
import java.util.stream.Collectors;

@Schema(name = "userData")
public class UserWrapper implements JsonApiIdentifiable{

	private final User user;

	private UserWrapper(String id, String name, String email, Role role) {
		this.user = new User(id, name, email, role);
	}


	@Schema(name = "attributes")
	public User getUser() {
		return user;
	}


	@Schema(example = "913fa0b7-0969-4438-9436-e5a8762bd5ab")
	@Override
	public String getId() {
		return user.getId();
	}

	@Override
	@Schema(allowableValues = "User")
	public String getType() {
		return User.class.getSimpleName();
	}


	public static UserWrapper from ( User user ) {
		return new UserWrapper(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getRole());
	}


	public static Collection<UserWrapper> fromCollection ( Collection<User> users ) {
		return users.stream()
			.map(UserWrapper::from)
			.collect(Collectors.toList());
	}
}
