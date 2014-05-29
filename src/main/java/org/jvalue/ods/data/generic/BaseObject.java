package org.jvalue.ods.data.generic;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

public class BaseObject extends GenericEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private Serializable object;
	
	
	public BaseObject(Serializable object)
	{
		this.setObject(object);	
	}

	@JsonValue
	public Serializable getObject() {
		return object;
	}

	public void setObject(Serializable object) {
		this.object = object;
	}
}
