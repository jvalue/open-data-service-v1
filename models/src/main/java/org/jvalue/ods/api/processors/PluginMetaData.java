package org.jvalue.ods.api.processors;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public final class PluginMetaData {

	@NotNull
	private final String id;

	@NotNull private final String author;

	@JsonCreator
	public PluginMetaData(
			@JsonProperty("id") String id,
			@JsonProperty("author") String author) {

		this.id = id;
		this.author = author;
	}


	public String getId() {
		return id;
	}


	public String getAuthor() {
		return author;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof PluginMetaData)) return false;
		if (other == this) return true;
		PluginMetaData metaData = (PluginMetaData) other;
		return Objects.equal(id, metaData.id)
				&& Objects.equal(author, metaData.author);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, author);
	}

}
