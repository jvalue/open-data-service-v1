/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.sources;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import io.swagger.v3.oas.annotations.media.Schema;

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


	@Schema(example = "de-pegelonline")
	public String getName() {
		return name;
	}


	@Schema(example = "pegelonline")
	public String getTitle() {
		return title;
	}


	@Schema(example = "Wasser- und Schifffahrtsverwaltung des Bundes (WSV)")
	public String getAuthor() {
		return author;
	}


	@Schema(example = "https://www.pegelonline.wsv.de/adminmail")
	public String getAuthorEmail() {
		return authorEmail;
	}


	@Schema(example =  "PEGELONLINE stellt kostenfrei tagesaktuelle Rohwerte verschiedener gewÃƒÂ¤sserkundlicher Parameter (z.B. Wasserstand)" +
		" der Binnen- und KÃƒÂ¼stenpegel der WasserstraÃƒÅ¸en des Bundes bis maximal 30 Tage rÃƒÂ¼ckwirkend zur Ansicht und zum Download bereit.")
	public String getNotes() {
		return notes;
	}


	@Schema(example = "https://www.pegelonline.wsv.de")
	public String getUrl() {
		return url;
	}


	@Schema(example = "http://www.pegelonline.wsv.de/gast/nutzungsbedingungen")
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
