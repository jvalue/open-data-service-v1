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

package org.jvalue.ods.adapter.pegelonline.data;

import org.ektorp.support.CouchDbDocument;

/**
 * The Class Comment.
 */
public class Comment extends CouchDbDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The long description. */
	private String longDescription;

	/** The short description. */
	private String shortDescription;

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
}
