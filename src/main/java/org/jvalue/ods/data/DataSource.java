/*
 * 
 */
package org.jvalue.ods.data;

import java.util.List;

import org.jvalue.ods.data.schema.Schema;

/**
 * The Class DataSource.
 */
public class DataSource {
	
	/** The url. */
	private final String url;
	
	/** The schema. */
	private final Schema schema;
	
	/** The attributes. */
	private final List<String> attributes;
	
	/**
	 * Instantiates a new data source.
	 *
	 * @param url the url
	 * @param schema the schema
	 * @param attributes the attributes
	 */
	public DataSource(String url, Schema schema, List<String> attributes)
	{
		this.url = url;
		this.schema = schema;
		this.attributes = attributes;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets the schema.
	 *
	 * @return the schema
	 */
	public Schema getSchema() {
		return schema;
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public List<String> getAttributes() {
		return attributes;
	}
}
