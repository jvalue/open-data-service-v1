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

import java.util.LinkedList;
import java.util.List;

/**
 * The Interface OdsFilter.
 */
public abstract class OdsFilter<P,R> {

	protected final List<OdsFilter<R,?>> filterChain = new LinkedList<OdsFilter<R,?>>();


	public final void addFilter(OdsFilter<R,?> filter) {
		if (filter == null) throw new NullPointerException("filter cannot be null");
		filterChain.add(filter);
	}


	public final boolean removeFilter(OdsFilter<R,?> filter) {
		if (filter == null) throw new NullPointerException("filter cannot be null");
		return filterChain.remove(filter);
	}


	public final boolean containsFilter(OdsFilter<R,?> filter) {
		return filterChain.contains(filter);
	}


	public void filter(P param) {
		R ret = filterHelper(param);

		for (OdsFilter<R,?> filter : filterChain) {
			filter.filter(ret);
		}
	}

	
	protected abstract R filterHelper(P param);
}
