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
 * The Class Measurement.
 */
public class Measurement extends CouchDbDocument {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The timestamp. */
	private String timestamp;

	/** The value. */
	private Number value;

	/**
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Sets the timestamp.
	 * 
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Number getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(Number value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return timestamp + "|" + value;

	}

}
