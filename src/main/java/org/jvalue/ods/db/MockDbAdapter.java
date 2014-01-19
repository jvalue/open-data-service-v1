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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * The Class MockDbAdapter.
 */
public class MockDbAdapter implements DbAdapter {

	/** The list. */
	Map<String, Object> list = new HashMap<String, Object>();
	
	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#getDocument(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> T getDocument(Class<T> c, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#getLastDocumentId()
	 */
	@Override
	public String getLastDocumentId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#insert(java.lang.Object)
	 */
	@Override
	public <T> void insert(T data) {
			if (data == null)
				throw new IllegalArgumentException("data is null");
			
		//list.put(new Guid().toString(), data);
	}

	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#update(java.lang.Object)
	 */
	@Override
	public void update(Object data) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#deleteDatabase()
	 */
	@Override
	public void deleteDatabase() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#connect()
	 */
	@Override
	public void connect() {}

	/* (non-Javadoc)
	 * @see org.jvalue.ods.db.DbAdapter#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return true;
	}

}
