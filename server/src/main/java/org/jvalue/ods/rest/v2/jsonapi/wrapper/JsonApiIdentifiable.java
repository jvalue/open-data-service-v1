/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.rest.v2.jsonapi.wrapper;

public interface JsonApiIdentifiable {
	String getId();
	String getType();

	default boolean isSame(JsonApiIdentifiable other) {
		return getId().equals(other.getId()) && getType().equals(other.getType());
	}
}
