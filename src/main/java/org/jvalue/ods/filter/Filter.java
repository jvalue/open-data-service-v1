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
package org.jvalue.ods.filter;

import org.jvalue.ods.utils.Assert;

public abstract class Filter<P, R> {

	protected Filter<R, ?> nextFilter;

	public Filter() { }


	public <T> Filter<R, T> setNextFilter(Filter<R, T> nextFilter) {
		Assert.assertNotNull(nextFilter);
		this.nextFilter = nextFilter;
		return nextFilter;
	}


	/**
	 * Called to process a new element of the current data stream.
	 */
	public void process(P param) throws FilterException {
		R result = doProcess(param);
		if (nextFilter != null) nextFilter.process(result);
	}


	/**
	 * Called when all elements of the current data stream have been processed. This method
	 * can be used to clean up resources, release resources etc.
	 */
	public final void onComplete() throws FilterException {
		doOnComplete();
		if (nextFilter != null) nextFilter.onComplete();
	}


	protected abstract R doProcess(P param) throws FilterException;
	protected abstract void doOnComplete() throws FilterException;

}
