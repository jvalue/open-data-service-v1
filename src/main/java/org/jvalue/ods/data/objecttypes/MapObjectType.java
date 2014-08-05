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

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ods.data.valuetypes.GenericValueType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The Class MapObjectType.
 */
@JsonInclude(Include.NON_NULL)
public class MapObjectType extends ObjectType {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private String name;

	private String dataQualityStatus;

	/** The attributes. */
	private Map<String, GenericValueType> attributes;

	/** The referenced objects. */
	private Map<String, ObjectType> referencedObjects;

	/**
	 * Instantiates a new map object type.
	 * 
	 * @param name
	 *            the name
	 * @param attributes
	 *            the attributes
	 * @param referencedObjects
	 *            the referenced objects
	 */
	public MapObjectType(String name, Map<String, GenericValueType> attributes,
			Map<String, ObjectType> referencedObjects) {
		this.name = name;
		this.attributes = attributes;
		if (this.attributes == null) {
			this.attributes = new HashMap<String, GenericValueType>();
		}

		this.referencedObjects = referencedObjects;
		if (this.referencedObjects == null) {
			this.referencedObjects = new HashMap<String, ObjectType>();
		}
		this.dataQualityStatus = "raw";
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the attributes.
	 * 
	 * @return the attributes
	 */
	public Map<String, GenericValueType> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attributes.
	 * 
	 * @param attributes
	 *            the attributes
	 */
	public void setAttributes(Map<String, GenericValueType> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Gets the referenced objects.
	 * 
	 * @return the referenced objects
	 */
	public Map<String, ObjectType> getReferencedObjects() {
		return referencedObjects;
	}

	/**
	 * Sets the referenced objects.
	 * 
	 * @param referencedObjects
	 *            the referenced objects
	 */
	public void setReferencedObjects(Map<String, ObjectType> referencedObjects) {
		this.referencedObjects = referencedObjects;
	}

	public String getDataQualityStatus() {
		return dataQualityStatus;
	}

	public void setDataQualityStatus(String dataQualityStatus) {
		this.dataQualityStatus = dataQualityStatus;
	}

}
