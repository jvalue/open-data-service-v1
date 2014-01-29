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

/**
 * The Class BoolValue.
 */
public class BoolValue extends GenericValue {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The bool. */
	private boolean bool;

	/** The type. */
	private final String type = "bool";

	/**
	 * Instantiates a new bool value.
	 * 
	 * @param bool
	 *            the bool
	 */
	public BoolValue(boolean bool) {
		this.bool = bool;
	}

	/**
	 * Gets the bool.
	 * 
	 * @return the bool
	 */
	public boolean getBool() {
		return bool;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

}
