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

import com.google.inject.Inject;

import org.jvalue.ods.administration.AdministrationLogging;
import org.jvalue.ods.filter.FilterChainManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.dropwizard.lifecycle.Managed;


class DataGrabberManager implements Managed {

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	private final GrabberRunnable grabberRunnable;
	private final long grabberUpdateInterval;
	private final FilterChainManager filterChainManager;


	@Inject
	DataGrabberManager(
			@GrabberUpdateInterval long grabberUpdateInterval,
			FilterChainManager filterChainManager) {

		this.grabberUpdateInterval = grabberUpdateInterval;
		this.filterChainManager = filterChainManager;
		this.grabberRunnable = new GrabberRunnable(filterChainManager);
	}


	@Override
	public void start() {
		executorService.scheduleAtFixedRate(grabberRunnable, 0, grabberUpdateInterval, TimeUnit.SECONDS);
	}


	@Override
	public void stop() {
		executorService.shutdown();
	}


	private static final class GrabberRunnable implements Runnable {

		private final FilterChainManager filterChainManager;

		public GrabberRunnable(FilterChainManager filterChainManager) {
			this.filterChainManager = filterChainManager;
		}


		@Override
		public void run() {
			/*
			// BIG TODO: this should happend somewhere else, but not here!
			AdministrationLogging.log("Initialize started");
			DbAccessor<JsonNode> accessor = DbFactory.createDbAccessor("ods");
			FilterChainManager filterManager = new FilterChainManager();
			ConfigurationManager.configureAll(accessor, filterManager);
			AdministrationLogging.log("Initialize completed");
			*/

			// accessor = DbFactory.createDbAccessor("ods");
			// accessor.connect();
			AdministrationLogging.log("Update started");
			filterChainManager.startFilterChains();
			AdministrationLogging.log("Update completed");
		}

	}

}
