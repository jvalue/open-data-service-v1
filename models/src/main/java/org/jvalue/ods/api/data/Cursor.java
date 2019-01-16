/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public final class Cursor {

	private final String next;
	private final boolean hasNext;
	private final int count;

	@JsonCreator
	public Cursor(

			@JsonProperty("next") String next,
			@JsonProperty("hasNext") boolean hasNext,
			@JsonProperty("count") int count) {

		this.next = next;
		this.hasNext = hasNext;
		this.count = count;
	}


	public String getNext() {
		return next;
	}


	public boolean getHasNext() {
		return hasNext;
	}


	public int getCount() {
		return count;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Cursor)) return false;
		Cursor cursor = (Cursor) other;
		return Objects.equal(next, cursor.next)
				&& Objects.equal(hasNext, cursor.hasNext)
				&& Objects.equal(count, cursor.count);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(next, hasNext, count);
	}

}
