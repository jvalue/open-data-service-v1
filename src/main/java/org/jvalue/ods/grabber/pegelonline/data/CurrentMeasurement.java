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
 * The Class CurrentMeasurement.
 */
public class CurrentMeasurement implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private String id;

	/** The revision. */
	private String revision;

	/** The state mnw mhw. */
	private String stateMnwMhw;

	/** The state nsw hsw. */
	private String stateNswHsw;

	/** The timestamp. */
	private String timestamp;

	/** The trend. */
	private Number trend;

	/** The value. */
	private Number value;

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
	 * Gets the state mnw mhw.
	 * 
	 * @return the state mnw mhw
	 */
	public String getStateMnwMhw() {
		return this.stateMnwMhw;
	}

	/**
	 * Sets the state mnw mhw.
	 * 
	 * @param stateMnwMhw
	 *            the new state mnw mhw
	 */
	public void setStateMnwMhw(String stateMnwMhw) {
		this.stateMnwMhw = stateMnwMhw;
	}

	/**
	 * Gets the state nsw hsw.
	 * 
	 * @return the state nsw hsw
	 */
	public String getStateNswHsw() {
		return this.stateNswHsw;
	}

	/**
	 * Sets the state nsw hsw.
	 * 
	 * @param stateNswHsw
	 *            the new state nsw hsw
	 */
	public void setStateNswHsw(String stateNswHsw) {
		this.stateNswHsw = stateNswHsw;
	}

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
	 * Gets the trend.
	 * 
	 * @return the trend
	 */
	public Number getTrend() {
		return this.trend;
	}

	/**
	 * Sets the trend.
	 * 
	 * @param trend
	 *            the new trend
	 */
	public void setTrend(Number trend) {
		this.trend = trend;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return "\n\t\t" + "timestamp: " + timestamp + "\n\t\t" + "value: "
				+ value + "\n\t\t" + "trend: " + trend + "\n\t\t"
				+ "stateMnwMhw: " + stateMnwMhw + "\n\t\t" + "stateNswHsw: "
				+ stateNswHsw;

	}

}
