/*
 * 
 */
package org.jvalue.ods.data;

import org.jvalue.ods.data.schema.Schema;

/**
 * The Class DataSource.
 */
public class DataSource {
	
	/** The url. */
	private final String url;
	
	/** The schema. */
	private final Schema schema;
	
	
	/**
	 * Instantiates a new data source.
	 *
	 * @param url the url
	 * @param schema the schema
	 */
	public DataSource(String url, Schema schema)
	{
		this.url = url;
		this.schema = schema;
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
}
