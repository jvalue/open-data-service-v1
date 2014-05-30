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
package org.jvalue.ods.data;

import org.jvalue.ods.data.schema.GenericObjectType;

/**
 * The Class DataSource.
 */
public class DataSource {

	/** The url. */
	private final String url;

	/** The data source schema. */
	private final GenericObjectType dataSourceSchema;

	/**
	 * Instantiates a new data source.
	 * 
	 * @param url
	 *            the url
	 * @param dataSourceSchema
	 *            the data source schema
	 */
	public DataSource(String url, GenericObjectType dataSourceSchema) {
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
	public GenericObjectType getDataSourceSchema() {
		return dataSourceSchema;
	}
}
