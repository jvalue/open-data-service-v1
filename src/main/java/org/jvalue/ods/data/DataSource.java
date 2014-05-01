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

	/** The data source schema. */
	private final Schema dataSourceSchema;

	/**
	 * Instantiates a new data source.
	 *
	 * @param url the url
	 * @param dataSourceSchema the data source schema
	 */
	public DataSource(String url, Schema dataSourceSchema) {
		this.url = url;
		this.dataSourceSchema = dataSourceSchema;
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
	 * Gets the data source schema.
	 * 
	 * @return the data source schema
	 */
	public Schema getDataSourceSchema() {
		return dataSourceSchema;
	}
}
