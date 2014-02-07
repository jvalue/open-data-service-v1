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

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A factory for creating DbAccessor objects.
 */
public class DbFactory {

	/**
	 * Creates a new DbAccessor object.
	 * 
	 * @param databaseName
	 *            the database name
	 * @return the db accessor
	 */
	public static DbAccessor<JsonNode> createDbAccessor(String databaseName) {
		return new CouchDbAccessor(databaseName);
	}

	/**
	 * Creates a new DbAccessor object.
	 * 
	 * @param databaseName
	 *            the database name
	 * @return the db accessor
	 */
	public static DbAccessor<JsonNode> createMockDbAccessor(String databaseName) {
		return new MockDbAccessor(databaseName);
	}

}
