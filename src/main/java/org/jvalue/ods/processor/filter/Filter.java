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
package org.jvalue.ods.processor.filter;

public interface Filter<P, R> {

	/**
	 * Called to process a new element of the current data stream.
	 */
	public void filter(P data) throws FilterException;


	/**
	 * Called when all elements of the current data stream have been processed. This method
	 * can be used to clean up resources, release resources etc.
	 */
	public void onComplete() throws FilterException;


	/**
	 * Sets the next filter in a filter chain.
	 * @return the passed in filter to allow method chaining.
	 */
	public <T> Filter<R, T> setNextFilter(Filter<R, T> filter);

}
