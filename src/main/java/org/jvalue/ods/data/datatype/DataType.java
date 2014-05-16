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
package org.jvalue.ods.data.datatype;

/**
 * The Class DataType.
 */
public class DataType {

	/** The name. */
	private String name;

	/** The data type. */
	private DataTypeEnum dataType;

	/**
	 * Instantiates a new data type.
	 * 
	 * @param name
	 *            the name
	 * @param dataType
	 *            the data type
	 */
	protected DataType(String name, DataTypeEnum dataType) {
		this.setName(name);
		this.setDataType(dataType);
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
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the data type.
	 * 
	 * @return the data type
	 */
	public DataTypeEnum getDataType() {
		return dataType;
	}

	/**
	 * Sets the data type.
	 * 
	 * @param dataType
	 *            the new data type
	 */
	private void setDataType(DataTypeEnum dataType) {
		this.dataType = dataType;
	}

}
