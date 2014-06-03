/*
 * 
 */
package org.jvalue.ods.data.generic;

import java.io.Serializable;

import org.jvalue.ods.data.schema.AllowedValueTypes;
import org.jvalue.ods.data.schema.GenericValueType;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Class BaseObject.
 */
public class BaseObject extends GenericEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5642565077433654663L;

	/** The object. */
	private Serializable object;

	/** The object type. */
	private GenericValueType objectType;

	/**
	 * Instantiates a new base object.
	 * 
	 * @param object
	 *            the object
	 */
	public BaseObject(Serializable object) {
		this.setObject(object);

		String name = object.getClass().getName();
		this.setObjectType(AllowedValueTypes.getGenericValueType(name));
	}

	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	@JsonValue
	public Serializable getObject() {
		return object;
	}

	/**
	 * Sets the object.
	 * 
	 * @param object
	 *            the new object
	 */
	public void setObject(Serializable object) {
		this.object = object;
	}

	/**
	 * Gets the object type.
	 * 
	 * @return the object type
	 */
	public GenericValueType getObjectType() {
		return objectType;
	}

	/**
	 * Sets the object type.
	 * 
	 * @param genericValueType
	 *            the new object type
	 */
	private void setObjectType(GenericValueType genericValueType) {
		this.objectType = genericValueType;
	}
}
