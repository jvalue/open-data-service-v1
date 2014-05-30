package org.jvalue.ods.data.generic;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Class BaseObject.
 */
public class BaseObject extends GenericEntity {

	private static final long serialVersionUID = 5642565077433654663L;
	
	/** The object. */
	private Serializable object;
	
	
	/**
	 * Instantiates a new base object.
	 *
	 * @param object the object
	 */
	public BaseObject(Serializable object)
	{
		this.setObject(object);	
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
	 * @param object the new object
	 */
	public void setObject(Serializable object) {
		this.object = object;
	}
}
