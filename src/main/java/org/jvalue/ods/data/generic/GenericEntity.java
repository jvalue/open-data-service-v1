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
package org.jvalue.ods.data.generic;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class GenericEntity.
 */
@JsonInclude(Include.NON_NULL)
public abstract class GenericEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7913898247847701123L;

	/** The id. */
	private String id;

	/** The revision. */
	private String revision;

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
