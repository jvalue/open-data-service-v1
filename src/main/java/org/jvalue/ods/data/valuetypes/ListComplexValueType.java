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
package org.jvalue.ods.data.valuetypes;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The Class ListSchema.
 */
@JsonInclude(Include.NON_NULL)
public class ListComplexValueType extends GenericValueType {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8492689528411698541L;
	
	/** The list. */
	private List<GenericValueType> list = new LinkedList<GenericValueType>();

	/**
	 * Instantiates a new list value.
	 * 
	 * @param list
	 *            the list
	 */
	public ListComplexValueType(List<GenericValueType> list) {
		this.list = list;
	}

	/**
	 * Gets the list.
	 * 
	 * @return the list
	 */
	@JsonValue
	public List<GenericValueType> getList() {
		return list;
	}

}
