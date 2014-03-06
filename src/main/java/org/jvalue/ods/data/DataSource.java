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
	
	/** The db schema. */
	private final Schema dbSchema;

	
	/**
	 * Instantiates a new data source.
	 *
	 * @param url the url
	 * @param dataSourceSchema the data source schema
	 * @param dbSchema the db schema
	 */
	public DataSource(String url, Schema dataSourceSchema, Schema dbSchema)
	{
		this.url = url;
		this.dataSourceSchema = dataSourceSchema;
		this.dbSchema = dbSchema;
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
	 * Gets the db schema.
	 *
	 * @return the db schema
	 */
	public Schema getDbSchema() {
		return dbSchema;
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
