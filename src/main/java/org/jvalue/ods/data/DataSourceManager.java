/*  Open Data Service

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

import com.google.inject.Inject;

import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.utils.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class DataSourceManager {

	// TODO persisting!

	private final Map<String, DataSourceConfiguration> configurations = new HashMap<>();
	private final FilterChainManager filterChainManager;

	@Inject
	DataSourceManager(FilterChainManager filterChainManager) {
		this.filterChainManager = filterChainManager;
	}


	public void addConfiguration(DataSourceConfiguration configuration) {
		Assert.assertNotNull(configuration);
		this.configurations.put(configuration.getDataSource().getSourceId(), configuration);
		filterChainManager.register(configuration.getFilterChain());
		// TODO insert db schema (raw and improved)
		// TODO insert metadata
		// TODO create common views??
	}


	public List<DataSource> getAllDataSources() {
		List<DataSource> sources = new LinkedList<DataSource>();
		for (DataSourceConfiguration config : configurations.values()) {
			sources.add(config.getDataSource());
		}
		return sources;
	}


	public DataRepository getDataRepositoryForSourceId(String sourceId) {
		Assert.assertNotNull(sourceId);
		return configurations.get(sourceId).getDataRepository();
	}

}
