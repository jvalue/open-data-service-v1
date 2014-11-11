/*  Open Data Service
    Copyright (C) 

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
package org.jvalue.ods.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The Class DeepCopy.
 */
public class DeepCopy {

	/**
	 * Copy object.
	 * 
	 * @param object
	 *            the object
	 * @return the object
	 */
	public static Object copyObject(Object object) {

		// json-specific hack, these nodes are not serializable
		if (object instanceof JsonNode) {
			return ((JsonNode) object).deepCopy();
		}

		ByteArrayOutputStream os = null;
		ByteArrayInputStream is = null;

		try {
			os = new ByteArrayOutputStream();
			new ObjectOutputStream(os).writeObject(object);
			is = new ByteArrayInputStream(os.toByteArray());
			return new ObjectInputStream(is).readObject();
		} catch (IOException | ClassNotFoundException e) {
			return null;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Log.error(e.getMessage());
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							Log.error(e.getMessage());
						}
					}
				}
			}
		}
	}

}
