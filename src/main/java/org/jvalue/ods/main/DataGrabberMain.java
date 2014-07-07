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

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.sources.OsmSource;
import org.jvalue.ods.data.sources.PegelOnlineSource;
import org.jvalue.ods.data.sources.PegelPortalMvSource;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.DbInsertionFilter;
import org.jvalue.ods.db.DbUtils;
import org.jvalue.ods.filter.FilterChain;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.notifications.NotificationFilter;
import org.jvalue.ods.qa.improvement.CombineSourceFilter;
import org.jvalue.ods.qa.improvement.RenameSourceFilter;
import org.jvalue.ods.translator.TranslatorFactory;

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

		Logging.adminLog("Initializing Ods");

		initialized = true;
		accessor = DbFactory.createDbAccessor("ods");
		accessor.connect();

		// create sources
		DataSourceManager sourceManager = DataSourceManager.getInstance();
		sourceManager.clearSources();
		sourceManager.addSource(PegelOnlineSource.createInstance());
		sourceManager.addSource(OsmSource.createInstance());
		sourceManager.addSource(PegelPortalMvSource.createInstance());

		// create filter chains
		FilterChainManager filterManager = FilterChainManager.getInstance();

		FilterChain<Void, GenericEntity> pegelOnlineChain = FilterChain
			.instance(TranslatorFactory.getPegelOnlineTranslator());
		pegelOnlineChain.setNextFilter(new DbInsertionFilter(accessor))
			.setNextFilter(new CombineSourceFilter())
			.setNextFilter(new RenameSourceFilter())
			.setNextFilter(new NotificationFilter());
		filterManager.register(PegelOnlineSource.createInstance(), pegelOnlineChain);

		FilterChain<Void, GenericEntity> pegelPortalChain = FilterChain
			.instance(TranslatorFactory.getPegelPortalMvTranslator());
		pegelOnlineChain.setNextFilter(new DbInsertionFilter(accessor))
			.setNextFilter(new NotificationFilter());
		filterManager.register(PegelPortalMvSource.createInstance(), pegelPortalChain);

		FilterChain<Void, GenericEntity> osmChain = FilterChain
			.instance(TranslatorFactory.getOsmTranslator());
		pegelOnlineChain.setNextFilter(new DbInsertionFilter(accessor))
			.setNextFilter(new NotificationFilter());
		filterManager.register(OsmSource.createInstance(), osmChain);


		// db initialization
		accessor.deleteDatabase();
		createCommonViews();
		for (DataSource source : sourceManager.getAllSources()) {
			accessor.insert(source.getDbSchema());
			accessor.insert(source.getMetaData());
			for (OdsView view : source.getOdsViews()) {
				DbUtils.createView(accessor, view);
			}
		}

		Logging.adminLog("Initialization completet");
	}


	public static boolean isInitialized() {
		return initialized;
	}


	public static void updateData() {
		Logging.adminLog("Update started");

		accessor = DbFactory.createDbAccessor("ods");
		accessor.connect();

		// start filtering
		for (DataSource source : DataSourceManager.getInstance().getAllSources()) {
			Logging.adminLog("grabbing " + source.getId() + " ...");
			FilterChainManager.getInstance().process(source);
		}
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
