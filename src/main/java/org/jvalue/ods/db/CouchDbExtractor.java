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
package org.jvalue.ods.db;


/**
 * The Class CouchDbExtractor.
 */
public class CouchDbExtractor extends CouchDbAccessor {

	/**
	 * Instantiates a new couch db extractor.
	 * 
	 * @param databaseName
	 *            the database name
	 */
	public CouchDbExtractor(String databaseName) {

		super(databaseName);

	}

	/**
	 * Gets the document.
	 * 
	 * @param <T>
	 * 
	 * @param c
	 *            the c
	 * @param id
	 *            the id
	 * @return the document
	 */
	public <T> T getDocument(Class<T> c, String id) {

		if (c == null) {
			throw new IllegalArgumentException("c is null");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is null");
		}

		return db.get(c, id);

	}

	public String getFirstDocumentId() {

		if (!db.getAllDocIds().isEmpty()) {
			return db.getAllDocIds().get(0);
		} else {
			return null;
		}
	}

}
