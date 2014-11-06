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

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ods.administration.AdministrationLogging;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbUtils;
import org.jvalue.ods.filter.FilterChainElement;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;

public final class ConfigurationManager {

	private ConfigurationManager() {
	}

	private static final List<Configuration> configurations = new LinkedList<Configuration>();
	static {
		configurations.add(new PegelOnlineConfiguration());
		configurations.add(new OsmConfiguration());
		configurations.add(new PegelPortalMvConfiguration());
	}

	public static void configureAll(DbAccessor<JsonNode> accessor,
			FilterChainManager filterManager) {

		Assert.assertNotNull(accessor, filterManager);

		AdministrationLogging.log("Initializing Ods");

		accessor.connect();
		accessor.deleteDatabase();
		createCommonViews(accessor);

		for (Configuration configuration : configurations) {
			DataSource source = configuration.getDataSource();
			FilterChainElement<Void, ?> chain = configuration.getFilterChain(accessor);

			filterManager.register(chain);

			accessor.insert(source.getImprovedDbSchema());

			if (source.getImprovedDbSchema() != null
					&& !source.getImprovedDbSchema().equals(
							source.getRawDbSchema())) {
				accessor.insert(source.getRawDbSchema());

			}

			accessor.insert(source.getMetaData());
			for (OdsView view : source.getOdsViews()) {
				DbUtils.createView(accessor, view);
			}
		}

		AdministrationLogging.log("Initialization completed");
	}

	private static void createCommonViews(DbAccessor<JsonNode> accessor) {
		DbUtils.createView(accessor, new OdsView("_design/ods",
				"getClassObjectByType",
				"function(doc) { if(doc.name) emit (doc.name, doc) }"));
		DbUtils.createView(accessor, new OdsView("_design/ods",
				"getAllClassObjects",
				"function(doc) { if(doc.name) emit (null, doc) }"));
	}

}
