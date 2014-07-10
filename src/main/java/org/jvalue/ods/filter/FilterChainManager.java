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

import java.util.HashSet;
import java.util.Set;

import org.jvalue.ods.utils.Assert;



public final class FilterChainManager {

	private static FilterChainManager instance;

	public static FilterChainManager getInstance() {
		if (instance == null) instance = new FilterChainManager();
		return instance;
	}



	private final Set<FilterChain<Void, ?>> filterChains = new HashSet<FilterChain<Void, ?>>();

	private FilterChainManager() { }


	public void register(FilterChain<Void, ?> chain) {
		Assert.assertNotNull(chain);
		filterChains.add(chain);
	}


	public void unregister(FilterChain<Void, ?> chain) {
		Assert.assertNotNull(chain);
		filterChains.remove(chain);
	}


	public boolean isRegistered(FilterChain<Void, ?> chain) {
		Assert.assertNotNull(chain);
		return filterChains.contains(chain);
	}


	public void startFilterChains() {
		for (FilterChain<Void, ?> chain : filterChains) {
			chain.filter(null);
		}
	}


	public Set<FilterChain<Void, ?>> getRegistered() {
		return new HashSet<FilterChain <Void,?>>(filterChains);
	}

}
