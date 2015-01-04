package org.jvalue.ods.api.sources;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public final class DataSourceMetaData {

	@NotNull private final String name, title, author, authorEmail, notes, url, termsOfUse;

	@JsonCreator
	public DataSourceMetaData(
			@JsonProperty("name") String name,
			@JsonProperty("title") String title,
			@JsonProperty("author") String author,
			@JsonProperty("authorEmail") String authorEmail,
			@JsonProperty("notes") String notes,
			@JsonProperty("url") String url,
			@JsonProperty("termsOfUse") String termsOfUse) {

		this.name = name;
		this.title = title;
		this.author = author;
		this.authorEmail = authorEmail;
		this.notes = notes;
		this.url = url;
		this.termsOfUse = termsOfUse;
	}


	public String getName() {
		return name;
	}


	public String getTitle() {
		return title;
	}


	public String getAuthor() {
		return author;
	}


	public String getAuthorEmail() {
		return authorEmail;
	}


	public String getNotes() {
		return notes;
	}


	public String getUrl() {
		return url;
	}


	public String getTermsOfUse() {
		return termsOfUse;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSourceMetaData)) return false;
		if (other == this) return true;
		DataSourceMetaData metaData = (DataSourceMetaData) other;
		return Objects.equal(name, metaData.name)
				&& Objects.equal(title, metaData.title)
				&& Objects.equal(author, metaData.author)
				&& Objects.equal(authorEmail, metaData.authorEmail)
				&& Objects.equal(notes, metaData.notes)
				&& Objects.equal(url, metaData.url)
				&& Objects.equal(termsOfUse, metaData.termsOfUse);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, title, author, authorEmail, notes, url, termsOfUse);
	}

}
