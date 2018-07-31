package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserWrapper extends User implements JsonApiIdentifiable{

	private UserWrapper(String id, String name, String email, Role role) {
		super(id, name, email, role);
	}


	@Override
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
