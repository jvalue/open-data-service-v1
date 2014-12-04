/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.utils.Assert;


public final class DataSourceMetaData extends CouchDbDocument {

	private final String name, title, author, authorEmail, notes, url, termsOfUse;

	public DataSourceMetaData(
			@JsonProperty("name") String name,
			@JsonProperty("title") String title,
			@JsonProperty("author") String author,
			@JsonProperty("authorEmail") String authorEmail,
			@JsonProperty("notes") String notes,
			@JsonProperty("url") String url,
			@JsonProperty("termsOfUse") String termsOfUse) {

		Assert.assertNotNull(name, title, authorEmail, notes, url, termsOfUse);
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
		DataSourceMetaData data = (DataSourceMetaData) other;
		return Objects.equal(name, data.name)
				&& Objects.equal(title, data.title)
				&& Objects.equal(author, data.author)
				&& Objects.equal(authorEmail, data.authorEmail)
				&& Objects.equal(notes, data.notes)
				&& Objects.equal(url, data.url)
				&& Objects.equal(termsOfUse, data.termsOfUse);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(name, title, author, authorEmail, notes, url, termsOfUse);
	}

}
