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
package org.jvalue.ods.configuration;

import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.FilterChainManager;

import java.util.LinkedList;
import java.util.List;

public final class ConfigurationManager {

	private final List<Configuration> configurations = new LinkedList<>();
	private final FilterChainManager filterChainManager;

	@Inject
	ConfigurationManager(FilterChainManager filterChainManager) {
		this.filterChainManager = filterChainManager;
	}


	public void addConfiguration(Configuration configuration) {
		this.configurations.add(configuration);
	}


	public void configureAll() {
		for (Configuration configuration : configurations) {
			DataSource source = configuration.getDataSource();
			filterChainManager.register(configuration.getFilterChain());
			// TODO insert db schema (raw and improved)
			// TODO insert metadata
			// TODO create common views??
		}
	}

	/*
	private static void createCommonViews(DbAccessor<JsonNode> accessor) {
		DbUtils.createView(accessor, new OdsView("_design/ods",
				"getClassObjectByType",
				"function(doc) { if(doc.name) emit (doc.name, doc) }"));
		DbUtils.createView(accessor, new OdsView("_design/ods",
				"getAllClassObjects",
				"function(doc) { if(doc.name) emit (null, doc) }"));
	}
	*/

}
