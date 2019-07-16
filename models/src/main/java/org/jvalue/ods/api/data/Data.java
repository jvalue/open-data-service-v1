/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.data;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Data {

	@NotNull private final List<JsonNode> result;
	@NotNull private final Cursor cursor;

	@JsonCreator
	public Data(
			@JsonProperty("result") List<JsonNode> result,
			@JsonProperty("cursor") Cursor cursor) {

		this.result = result;
		this.cursor = cursor;
	}


	public List<JsonNode> getResult() {
		return result;
	}


	public Cursor getCursor() {
		return cursor;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Data)) return false;
		Data data = (Data) other;
		return Objects.equal(result, data.result)
				&& Objects.equal(cursor, data.cursor);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(result, cursor);
	}

}
