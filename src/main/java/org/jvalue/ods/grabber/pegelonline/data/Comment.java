/*
    Open Data Service
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

package org.jvalue.ods.grabber.pegelonline.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class Comment.
 */
public class Comment implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private String id;

	/** The revision. */
	private String revision;

	/** The long description. */
	private String longDescription;

	/** The short description. */
	private String shortDescription;

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
	 * @param id
	 *            the new id
	 */
	@JsonProperty("_id")
	public void setId(String id) {
		this.id = id;
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
	 * @param revision
	 *            the new revision
	 */
	@JsonProperty("_rev")
	public void setRevision(String revision) {
		this.revision = revision;
	}

	/**
	 * Gets the long description.
	 * 
	 * @return the long description
	 */
	public String getLongDescription() {
		return this.longDescription;
	}

	/**
	 * Sets the long description.
	 * 
	 * @param longDescription
	 *            the new long description
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Gets the short description.
	 * 
	 * @return the short description
	 */
	public String getShortDescription() {
		return this.shortDescription;
	}

	/**
	 * Sets the short description.
	 * 
	 * @param shortDescription
	 *            the new short description
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return longDescription;

	}

}
