package org.jvalue.common.auth;


import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

/**
 * One authenticated user.
 */
public final class User {

	@NotNull private final String name;
	@NotNull private final Role role;

	public User(String name, Role role) {
		this.name = name;
		this.role = role;
	}


	public String getName() {
		return name;
	}


	public Role getRole() {
		return role;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof User)) return false;
		User user = (User) other;
		return Objects.equal(name, user.name)
				&& Objects.equal(role, user.role);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, role);
	}

}
