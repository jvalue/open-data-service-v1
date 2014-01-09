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
package org.jvalue.ods.adapter.pegelonline.data;

import java.util.List;

import org.ektorp.support.CouchDbDocument;

/**
 * The Class PegelOnlineData.
 */
public class PegelOnlineData extends CouchDbDocument {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private final String name = "PegelOnline";

	/** The publisher. */
	private final String publisher = "Wasser- und Schifffahrtsverwaltung des Bundes (WSV)";

	/** The source. */
	private final String source = "www.pegelonline.wsv.de/";

	/** The stations. */
	private List<Station> stations;

	/**
	 * Instantiates a new pegel online data.
	 * 
	 * @param stations
	 *            the stations
	 */
	public PegelOnlineData(List<Station> stations) {
		this.stations = stations;
	}

	/**
	 * Instantiates a new pegel online data.
	 */
	public PegelOnlineData() {

	}

	/**
	 * Gets the stations.
	 * 
	 * @return the stations
	 */
	public List<Station> getStations() {
		return stations;
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

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the publisher.
	 * 
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
}
