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

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Class ListValue.
 */
@JsonInclude(Include.NON_NULL)
public class ListValue extends GenericValue {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The list. */
	private List<GenericValue> list = new LinkedList<GenericValue>();

	/**
	 * Instantiates a new list value.
	 */
	public ListValue() {
	}

	/**
	 * Instantiates a new list value.
	 * 
	 * @param list
	 *            the list
	 */
	@JsonCreator
	public ListValue(List<GenericValue> list) {
		this.list = list;
	}

	/**
	 * Gets the list.
	 * 
	 * @return the list
	 */
	@JsonValue
	public List<GenericValue> getList() {
		return list;
	}

}
