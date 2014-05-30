/*
 * 
 */
package org.jvalue.ods.data.schema;

/**
 * The Class BaseObjectType.
 */
public class BaseObjectType extends GenericObjectType {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2148204345426479777L;
	
	/** The name. */
	private String name;
	
	/** The type. */
	private ObjectTypeEnum type;
	
	/**
	 * Instantiates a new base object type.
	 *
	 * @param name the name
	 * @param type the type
	 */
	public BaseObjectType(String name, ObjectTypeEnum type) {
		this.setName(name);
		this.setType(type);
	}
	
	

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	private void setType(ObjectTypeEnum type) {
		this.type = type;		
	}



	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	private void setName(String name) {
		this.name = name;		
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
	 * Gets the type.
	 *
	 * @return the type
	 */
	public ObjectTypeEnum getType() {
		return type;
	}

}
