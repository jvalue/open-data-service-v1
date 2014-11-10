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
package org.jvalue.ods.grabber;

import com.fasterxml.jackson.databind.JsonNode;

import org.jvalue.ods.administration.AdministrationLogging;
import org.jvalue.ods.configuration.ConfigurationManager;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.logger.Logging;

import io.dropwizard.lifecycle.Managed;


public class DataGrabberMain implements Managed {

	private final DataGrabberThread grabberThread = new DataGrabberThread();

	@Override
	public void start() {
		new Thread(grabberThread).start();
	}


	@Override
	public void stop() {
		grabberThread.stop();
	}


	private static final class DataGrabberThread implements Runnable {

		private boolean stopped = false;

		@Override
		public void run() {
			while (true) {
				try {
					if (stopped) return;
					grabData();
				} catch (Exception ex) {
					Logging.error(DataGrabberMain.class, ex.getMessage());
					ex.printStackTrace();
				} finally {
					try {
						Thread.sleep(2000000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}


		private void grabData() {
			AdministrationLogging.log("Initialize started");
			DbAccessor<JsonNode> accessor = DbFactory.createDbAccessor("ods");
			FilterChainManager filterManager = new FilterChainManager();
			ConfigurationManager.configureAll(accessor, filterManager);
			AdministrationLogging.log("Initialize completed");

			AdministrationLogging.log("Update started");
			accessor = DbFactory.createDbAccessor("ods");
			accessor.connect();
			filterManager.startFilterChains();
			AdministrationLogging.log("Update completed");
		}


		public void stop() {
			this.stopped = true;
		}

	}

}
