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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Objects;
import com.google.inject.Inject;

import org.ektorp.DocumentNotFoundException;
import org.jvalue.ods.db.FilterChainReferenceRepository;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.dropwizard.lifecycle.Managed;


public final class FilterChainManager implements Managed {

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	private final FilterChainFactory filterChainFactory;
	private final FilterChainReferenceRepository referenceRepository;

	private final Map<FilterKey, ScheduledFuture<?>> runningTasks = new HashMap<>();


	@Inject
	FilterChainManager(
			FilterChainFactory filterChainFactory,
			FilterChainReferenceRepository referenceRepository) {

		this.filterChainFactory = filterChainFactory;
		this.referenceRepository = referenceRepository;
	}


	public void add(FilterChainReference reference) {
		Assert.assertNotNull(reference);
		referenceRepository.add(reference);
		startFilterChain(reference);
	}


	public void remove(FilterChainReference reference) {
		Assert.assertNotNull(reference);
		referenceRepository.remove(reference);
		ScheduledFuture<?> task = runningTasks.remove(new FilterKey(reference.getDataSourceId(), reference.getFilterChainId()));
		task.cancel(false);
	}


	public FilterChainReference get(String dataSourceId, String filterChainId) {
		Assert.assertNotNull(dataSourceId, filterChainId);
		return referenceRepository.findByFilterChainAndSourceId(dataSourceId, filterChainId);
	}


	public List<FilterChainReference> getAllForSource(String dataSourceId) {
		Assert.assertNotNull(dataSourceId);
		return referenceRepository.findByDataSourceId(dataSourceId);
	}


	public boolean filterChainExists(String sourceId, String filterChainId) {
		try {
			get(sourceId, filterChainId);
			return true;
		} catch (DocumentNotFoundException dnfe) {
			return false;
		}
	}


	@Override
	public void start() {
		for (FilterChainReference reference : referenceRepository.getAll()) {
			startFilterChain(reference);
		}
	}


	@Override
	public void stop() {
		executorService.shutdown();
	}


	private void startFilterChain(FilterChainReference reference) {
		FilterKey key = new FilterKey(reference.getDataSourceId(), reference.getFilterChainId());
		ScheduledFuture<?> task = executorService.scheduleAtFixedRate(
				new FilterRunnable(key),
				0,
				reference.getMetaData().getExecutionPeriod(),
				TimeUnit.SECONDS);

		runningTasks.put(key, task);
	}


	private final class FilterRunnable implements Runnable {

		private final FilterKey key;

		public FilterRunnable(FilterKey key) {
			this.key = key;
		}


		@Override
		public void run() {
			try {
				Log.info("starting filter chain \"" + key.filterChainId + "\" for source \"" + key.dataSourceId + "\"");
				Filter<Void, ArrayNode> filterChain = filterChainFactory.createFilterChain(get(key.dataSourceId, key.filterChainId));
				filterChain.filter(null);
				Log.info("stopping filter chain \"" + key.filterChainId + "\" for source \"" + key.dataSourceId + "\"");
			} catch (Throwable throwable) {
				Log.error("error while running filter chain", throwable);
			}
		}

	}


	private static final class FilterKey {
		private final String dataSourceId, filterChainId;

		public FilterKey(String dataSourceId, String filterChainId) {
			this.dataSourceId = dataSourceId;
			this.filterChainId = filterChainId;
		}


		@Override
		public boolean equals(Object other) {
			if (other == null || !(other instanceof FilterKey)) return false;
			if (other == this) return true;
			FilterKey key = (FilterKey) other;
			return Objects.equal(dataSourceId, key.dataSourceId)
					&& Objects.equal(filterChainId, key.filterChainId);
		}


		@Override
		public int hashCode() {
			return Objects.hashCode(dataSourceId, filterChainId);
		}

	}
}
