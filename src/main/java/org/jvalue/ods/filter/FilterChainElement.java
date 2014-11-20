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
import org.jvalue.ods.utils.DeepCopy;

public final class FilterChainElement<P, R> {

	public static <P, R> FilterChainElement<P, R> instance(Filter<P, R> filter) {
		return new FilterChainElement<P, R>(filter);
	}

	private final Filter<P, R> filter;
	private FilterChainElement<R, ?> nextChain;

	private FilterChainElement(Filter<P, R> filter) {
		Assert.assertNotNull(filter);
		this.filter = filter;
	}

	public <T> FilterChainElement<R, T> setNextFilter(Filter<R, T> filter) {
		Assert.assertNotNull(filter);
		FilterChainElement<R, T> nextChain = new FilterChainElement<R, T>(filter);
		this.nextChain = nextChain;
		return nextChain;
	}

	public void filter(P param) throws FilterException {

		// maybe some other generics magic would be nicer, but P
		// does not always implement serializable for example
		@SuppressWarnings("unchecked")
		P copy = (P) DeepCopy.copyObject(param);

		R ret = filter.filter(copy);
		if (nextChain != null)
			nextChain.filter(ret);
	}

}
