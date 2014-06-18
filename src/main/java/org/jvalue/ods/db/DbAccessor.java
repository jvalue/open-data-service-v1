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

import java.util.Collection;
import java.util.List;

import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.data.objecttypes.ObjectType;

public interface DbAccessor<Q> {

	public <T> T getDocument(Class<T> c, String id);
	public void insert(Object data);
	public void update(Object data);
	public void delete(Object data);
	public void deleteDatabase();
	public void connect();
	boolean isConnected();
	public List<Q> getAllDocuments();
	public List<Q> executeDocumentQuery(String designDocId, String viewName, String key);
	public void executeBulk(Collection<MapObject> objects, ObjectType mapObjectType);

}
