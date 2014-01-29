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
package org.jvalue.ods.data.metadata;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The Class OdsMetaData.
 */
abstract class OdsMetaData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The date. */
	private String date;

	// metadata loosely based on
	// https://github.com/fraunhoferfokus/ogd-metadata/blob/master/OGPD_JSON_Schema.json
	// at first

	/** The name. */
	private String name;

	/** The title. */
	private String title;

	/** The author. */
	private String author;

	/** The author_email. */
	private String author_email;

	/** The notes. */
	private String notes;

	/** The url. */
	private String url;

	/** The terms_of_use. */
	private String terms_of_use;

	/**
	 * Instantiates a new ods meta data.
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
	public OdsMetaData(String name, String title, String author,
			String author_email, String notes, String url, String terms_of_use) {
		this.name = name;
		this.title = title;
		this.author = author;
		this.author_email = author_email;
		this.notes = notes;
		this.url = url;
		this.terms_of_use = terms_of_use;

		this.date = new Timestamp(System.currentTimeMillis()).toString();
	}

	/**
	 * Instantiates a new ods meta data.
	 */
	public OdsMetaData() {
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the author.
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the author_email.
	 * 
	 * @return the author_email
	 */
	public String getAuthor_email() {
		return author_email;
	}

	/**
	 * Gets the notes.
	 * 
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets the terms_of_use.
	 * 
	 * @return the terms_of_use
	 */
	public String getTerms_of_use() {
		return terms_of_use;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the new date
	 */
	public void setDate(String date) {
		this.date = date;
	}

}
