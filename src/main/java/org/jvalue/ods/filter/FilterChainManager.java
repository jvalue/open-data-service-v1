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

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.utils.Assert;



public final class FilterChainManager {

	private static FilterChainManager instance;

	public static FilterChainManager getInstance() {
		if (instance == null) instance = new FilterChainManager();
		return instance;
	}



	private final Map<Class<?>, FilterChain<Void, ?>> filterChains = new HashMap<>();

	private FilterChainManager() { }


	public void register(DataSource source, FilterChain<Void, ?> chain) {
		Assert.assertNotNull(source, chain);
		filterChains.put(source.getClass(), chain);
	}


	public void unregister(DataSource source) {
		Assert.assertNotNull(source);
		filterChains.remove(source.getClass());
	}


	public boolean isRegistered(DataSource source) {
		Assert.assertNotNull(source);
		return filterChains.containsKey(source.getClass());
	}


	public void process(DataSource source) {
		Assert.assertTrue(isRegistered(source), "No filter chain found");
		filterChains.get(source.getClass()).filter(source, null);
	}

}
