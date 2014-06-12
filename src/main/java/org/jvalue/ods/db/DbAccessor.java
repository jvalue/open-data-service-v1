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

/**
 * The Interface DbAdapter. Q is the type of query result
 * 
 * @param <Q>
 *            the generic type
 */
public interface DbAccessor<Q> {

	/**
	 * Gets the document.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param c
	 *            the c
	 * @param id
	 *            the id
	 * @return the document
	 */
	public <T> T getDocument(Class<T> c, String id);

	/**
	 * Insert.
	 * 
	 * @param data
	 *            the data
	 */
	public void insert(Object data);

	/**
	 * Update.
	 * 
	 * @param data
	 *            the data
	 * @return the string
	 */
	public void update(Object data);

	/**
	 * Delete database.
	 */
	public void deleteDatabase();

	/**
	 * Connect.
	 */
	public void connect();

	/**
	 * Checks if is connected.
	 * 
	 * @return true, if is connected
	 */
	boolean isConnected();

	/**
	 * Gets the all documents.
	 * 
	 * @return the all documents
	 */
	public List<Q> getAllDocuments();

	/**
	 * Execute document query.
	 * 
	 * @param designDocId
	 *            the design doc id
	 * @param viewName
	 *            the view name
	 * @param key
	 *            the key
	 * @return the object
	 */
	public List<Q> executeDocumentQuery(String designDocId, String viewName,
			String key);

	/**
	 * Execute bulk.
	 * 
	 * @param objects
	 *            the objects
	 * @param mapObjectType
	 *            the schema
	 */
	public void executeBulk(Collection<MapObject> objects, ObjectType mapObjectType);

}
