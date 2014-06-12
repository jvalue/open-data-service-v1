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
package org.jvalue.ods.data.objecttypes;

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ods.data.valuetypes.GenericValueType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The Class ListObjectType.
 */
@JsonInclude(Include.NON_NULL)
public class ListObjectType extends ObjectType {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The attributes. */
	private List<GenericValueType> attributes;
	
	/** The referenced objects. */
	private List<ObjectType> referencedObjects;

	/**
	 * Instantiates a new list object type.
	 *
	 * @param attributes the attributes
	 * @param referencedObjects the referenced objects
	 */
	public ListObjectType(List<GenericValueType> attributes,
			List<ObjectType> referencedObjects) {

		this.attributes = attributes;
		if (this.attributes == null) {
			this.attributes = new LinkedList<GenericValueType>();
		}

		this.referencedObjects = referencedObjects;
		if (this.referencedObjects == null) {
			this.referencedObjects = new LinkedList<ObjectType>();
		}
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public List<GenericValueType> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attributes.
	 *
	 * @param attributes the new attributes
	 */
	public void setAttributes(List<GenericValueType> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the referenced objects.
	 *
	 * @return the referenced objects
	 */
	public List<ObjectType> getReferencedObjects() {
		return referencedObjects;
	}

	/**
	 * Sets the referenced objects.
	 *
	 * @param referencedObjects the new referenced objects
	 */
	public void setReferencedObjects(List<ObjectType> referencedObjects) {
		this.referencedObjects = referencedObjects;
	}
}
