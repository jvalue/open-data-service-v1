package org.jvalue.ods.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GenericData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	private final Map<String, ValueType<?,?>> attributes;
	
	public GenericData()
	{
		attributes = new HashMap<String, ValueType<?,?>>();
	}
	
	public GenericData(Map<String, ValueType<?,?>> attributes)
	{
		this.attributes = attributes;
	}
	
	public void addAttribute(String keyName, ValueType<?,?> valueType)
	{
		attributes.put(keyName, valueType);
	}
	
	public ValueType<?,?> getAttribute(String keyName)
	{
		return attributes.get(keyName);
	}
}
