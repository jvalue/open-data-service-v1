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
import org.jvalue.ods.utils.Log;

import java.util.HashSet;
import java.util.Set;



public final class FilterChainManager {

	private final Set<FilterChainElement<Void, ?>> filterChains = new HashSet<FilterChainElement<Void, ?>>();


	FilterChainManager() { }


	public void register(FilterChainElement<Void, ?> chain) {
		Assert.assertNotNull(chain);
		filterChains.add(chain);
	}


	public void unregister(FilterChainElement<Void, ?> chain) {
		Assert.assertNotNull(chain);
		filterChains.remove(chain);
	}


	public boolean isRegistered(FilterChainElement<Void, ?> chain) {
		Assert.assertNotNull(chain);
		return filterChains.contains(chain);
	}


	public void startFilterChains() {
		for (FilterChainElement<Void, ?> chain : filterChains) {
			try {
				chain.filter(null);
			} catch (FilterException e) {
				Log.error("error while running filter", e);
			}
		}
	}


	public Set<FilterChainElement<Void, ?>> getRegistered() {
		return new HashSet<FilterChainElement<Void,?>>(filterChains);
	}

}
