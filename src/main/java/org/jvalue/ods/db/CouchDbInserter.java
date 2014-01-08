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

import org.ektorp.support.CouchDbDocument;

/**
 * The Class CouchDbInserter.
 */
public class CouchDbInserter extends CouchDbAccessor {

	/**
	 * Instantiates a new couch db inserter.
	 * 
	 * @param databaseName
	 *            the database name
	 */
	public CouchDbInserter(String databaseName) {
		super(databaseName);
	}

	/**
	 * Insert.
	 * 
	 * @param data
	 *            the data
	 */
	public void insert(CouchDbDocument data) {

		if (data == null)
			throw new IllegalArgumentException("data is null");

		db.create(data);
	}

}
