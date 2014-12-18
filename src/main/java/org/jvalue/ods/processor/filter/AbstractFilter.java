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

import org.jvalue.ods.utils.Assert;

abstract class AbstractFilter<P, R> implements Filter<P, R> {

	protected Filter<R, ?> nextFilter;

	public AbstractFilter() { }


	@Override
	public <T> Filter<R, T> setNextFilter(Filter<R, T> nextFilter) {
		Assert.assertNotNull(nextFilter);
		this.nextFilter = nextFilter;
		return nextFilter;
	}


	@Override
	public void filter(P data) throws FilterException {
		R result = doProcess(data);
		if (nextFilter != null) nextFilter.filter(result);
	}


	@Override
	public final void onComplete() throws FilterException {
		doOnComplete();
		if (nextFilter != null) nextFilter.onComplete();
	}


	protected abstract R doProcess(P data) throws FilterException;
	protected abstract void doOnComplete() throws FilterException;

}
