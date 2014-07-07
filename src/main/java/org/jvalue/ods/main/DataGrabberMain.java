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
package org.jvalue.ods.main;

import org.jvalue.ods.configuration.ConfigurationManager;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.DbUtils;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.databind.JsonNode;


public class DataGrabberMain {

	private static DbAccessor<JsonNode> accessor;
	private static boolean initialized = false;

	public static void main(String[] args) {
		initialize();
		updateData();
	}



	public static void initialize() {
		// TODO disabled for now as current insertion workflow requires db to be deleted
		// before each update
		// if (initialized) throw new IllegalStateException("Already initialized");

		ConfigurationManager.getInstance().configureAll();
	}


	public static boolean isInitialized() {
		return initialized;
	}


	public static void updateData() {
		Logging.adminLog("Update started");

		accessor = DbFactory.createDbAccessor("ods");
		accessor.connect();

		FilterChainManager.getInstance().startFilterChains();

		Logging.adminLog("Update completed");
	}


	private static void createCommonViews() {
		DbUtils.createView(
				accessor, 
				new OdsView(
					"_design/ods", 
					"getClassObjectByType",
					"function(doc) { if(doc.objectType) emit (doc.objectType, doc) }"));
		DbUtils.createView(
				accessor, 
				new OdsView(
					"_design/ods", 
					"getAllClassObjects",
					"function(doc) { if(doc.objectType) emit (null, doc) }"));
	}


}
