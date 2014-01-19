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
 * A factory for creating Db objects.
 */
public class DbFactory {
	
	/**
	 * Creates a new Db object.
	 *
	 * @param databaseName the database name
	 * @return the db adapter
	 */
	public static DbAdapter createCouchDbAdapter(String databaseName){
		if (isUnitTest())
			return new MockDbAdapter();
		
		return new CouchDbAdapter(databaseName);		
	}
	
	/**
	 * Checks if is unit test.
	 *
	 * @return true, if is unit test
	 */
	private static boolean isUnitTest()
	{
		//TODO check for unit test f.e. using argument in unit test configuration
		return false;
	}
	

}
