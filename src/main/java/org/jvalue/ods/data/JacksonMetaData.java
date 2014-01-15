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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class JacksonMetaData.
 */
@JsonInclude(Include.NON_NULL)
public class JacksonMetaData extends OdsMetaData {

	/** The id. */
	private String id;

	/** The revision. */
	private String revision;

	/**
	 * Instantiates a new jackson meta data.
	 * 
	 * @param name
	 *            the name
	 * @param title
	 *            the title
	 * @param author
	 *            the author
	 * @param author_email
	 *            the author_email
	 * @param notes
	 *            the notes
	 * @param url
	 *            the url
	 * @param terms_of_use
	 *            the terms_of_use
	 */
	public JacksonMetaData(String name, String title, String author,
			String author_email, String notes, String url, String terms_of_use) {
		super(name, title, author, author_email, notes, url, terms_of_use);

	}

	/**
	 * Instantiates a new jackson meta data.
	 */
	public JacksonMetaData() {
		super();
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@JsonProperty("_id")
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param s
	 *            the new id
	 */
	@JsonProperty("_id")
	public void setId(String s) {
		id = s;
	}

	/**
	 * Gets the revision.
	 * 
	 * @return the revision
	 */
	@JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}

	/**
	 * Sets the revision.
	 * 
	 * @param s
	 *            the new revision
	 */
	@JsonProperty("_rev")
	public void setRevision(String s) {
		revision = s;
	}

}
