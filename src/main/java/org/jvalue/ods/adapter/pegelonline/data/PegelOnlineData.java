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
import org.jvalue.ods.data.JacksonMetaData;

/**
 * The Class PegelOnlineData.
 */
public class PegelOnlineData extends CouchDbDocument {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	// metadata loosely based on
	// https://github.com/fraunhoferfokus/ogd-metadata/blob/master/OGPD_JSON_Schema.json
	// at first

	/** The jmd. */
	private JacksonMetaData metaData;

	// constants for metadata object, only used in constructor
	/** The name. */
	private final String name = "de-pegelonline";

	/** The title. */
	private final String title = "pegelonline";

	/** The author. */
	private final String author = "Wasser- und Schifffahrtsverwaltung des Bundes (WSV)";

	/** The author_email. */
	private final String author_email = "https://www.pegelonline.wsv.de/adminmail";

	/** The notes. */
	private final String notes = "PEGELONLINE stellt kostenfrei tagesaktuelle Rohwerte verschiedener gewässerkundlicher Parameter (z.B. Wasserstand) der Binnen- und Küstenpegel der Wasserstraßen des Bundes bis maximal 30 Tage rückwirkend zur Ansicht und zum Download bereit.";

	/** The url. */
	private final String url = "https://www.pegelonline.wsv.de";

	/** The terms_of_use. */
	private final String terms_of_use = "http://www.pegelonline.wsv.de/gast/nutzungsbedingungen";

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
		this.metaData = new JacksonMetaData(name, title, author, author_email,
				notes, url, terms_of_use);
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
	 * Gets the jmd.
	 *
	 * @return the jmd
	 */
	public JacksonMetaData getMetaData() {
		return metaData;
	}

	/**
	 * Sets the omd.
	 *
	 * @param jmd the new omd
	 */
	public void setMetaData(JacksonMetaData jmd) {
		this.metaData = jmd;
	}

}