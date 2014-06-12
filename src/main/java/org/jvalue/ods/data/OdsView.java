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
 * The Class OdsView.
 */
public class OdsView {

	/** The id path. */
	private String idPath;

	/** The view name. */
	private String viewName;

	/** The function. */
	private String function;

	/**
	 * Instantiates a new ods view.
	 * 
	 * @param idPath
	 *            the id path
	 * @param viewName
	 *            the view name
	 * @param function
	 *            the function
	 */
	public OdsView(String idPath, String viewName, String function) {		
		this.idPath = idPath;
		this.viewName = viewName;
		this.function = function;
	}

	/**
	 * Gets the view name.
	 * 
	 * @return the view name
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Sets the view name.
	 * 
	 * @param viewName
	 *            the new view name
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * Gets the id path.
	 * 
	 * @return the id path
	 */
	public String getIdPath() {
		return idPath;
	}

	/**
	 * Sets the id path.
	 * 
	 * @param idPath
	 *            the new id path
	 */
	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	/**
	 * Gets the function.
	 * 
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Sets the function.
	 * 
	 * @param function
	 *            the new function
	 */
	public void setFunction(String function) {
		this.function = function;
	}
}
