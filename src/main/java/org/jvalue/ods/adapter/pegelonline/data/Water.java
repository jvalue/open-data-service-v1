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

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class Water.
 */
public class Water implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private String id;

	/** The revision. */
	private String revision;

	/** The longname. */
	private String longname;

	/** The shortname. */
	private String shortname;

	/** The stations. */
	private List<Station> stations;

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
	 * Gets the longname.
	 * 
	 * @return the longname
	 */
	public String getLongname() {
		return this.longname;
	}

	/**
	 * Sets the longname.
	 * 
	 * @param longname
	 *            the new longname
	 */
	public void setLongname(String longname) {
		this.longname = longname;
	}

	/**
	 * Gets the shortname.
	 * 
	 * @return the shortname
	 */
	public String getShortname() {
		return this.shortname;
	}

	/**
	 * Sets the shortname.
	 * 
	 * @param shortname
	 *            the new shortname
	 */
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	/**
	 * Gets the stations.
	 * 
	 * @return the stations
	 */
	public List<Station> getStations() {
		return this.stations;
	}

	/**
	 * Sets the stations.
	 * 
	 * @param stations
	 *            the new stations
	 */
	public void setStations(List<Station> stations) {
		this.stations = stations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return longname;

	}

}
