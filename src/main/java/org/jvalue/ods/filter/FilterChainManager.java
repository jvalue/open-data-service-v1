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
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.FilterChainReferenceRepository;
import org.jvalue.ods.db.RepositoryCache;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


public final class FilterChainManager {

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	private final FilterChainFactory filterChainFactory;
	private final RepositoryCache<FilterChainReferenceRepository> referenceRepositoryCache;
	private final DbFactory dbFactory;

	private final Map<FilterKey, ScheduledFuture<?>> runningTasks = new HashMap<>();


	@Inject
	FilterChainManager(
			FilterChainFactory filterChainFactory,
			RepositoryCache<FilterChainReferenceRepository> referenceRepositoryCache,
			DbFactory dbFactory) {

		this.filterChainFactory = filterChainFactory;
		this.referenceRepositoryCache = referenceRepositoryCache;
		this.dbFactory = dbFactory;
	}


	public void add(DataSource source, DataRepository dataRepository, FilterChainReference reference) {
		Assert.assertNotNull(reference);

		// create filter chain repository
		FilterChainReferenceRepository referenceRepository = createFilterChainRepository(source);

		// add reference
		referenceRepository.add(reference);

		// start filter chain
		startFilterChain(source, dataRepository, reference);
	}


	public void remove(DataSource source, FilterChainReference reference) {
		Assert.assertNotNull(reference);

		// remove from repository
		referenceRepositoryCache.getForKey(source.getSourceId()).remove(reference);

		// stop running task
		ScheduledFuture<?> task = runningTasks.remove(new FilterKey(source.getSourceId(), reference.getFilterChainId()));
		task.cancel(false);
	}


	public void removeAll(DataSource source) {
		referenceRepositoryCache.remove(source.getSourceId());
	}


	public FilterChainReference get(DataSource source, String filterChainId) {
		Assert.assertNotNull(source, filterChainId);
		return referenceRepositoryCache.getForKey(source.getSourceId()).findByFilterChainId(filterChainId);
	}


	public List<FilterChainReference> getAllForSource(DataSource source) {
		Assert.assertNotNull(source);
		return referenceRepositoryCache.getForKey(source.getSourceId()).getAll();
	}


	public boolean filterChainExists(DataSource source, String filterChainId) {
		try {
			referenceRepositoryCache.getForKey(source.getSourceId()).get(filterChainId);
			return true;
		} catch (DocumentNotFoundException dnfe) {
			return false;
		}
	}


	/**
	 * Called once during lifecycle initialization.
	 * @param sources All sources including their data repositories to create the acutal
	 *                filter chain and start them.
	 */
	public void startAllFilterChains(Map<DataSource, DataRepository> sources) {
		for (Map.Entry<DataSource, DataRepository> sourceEntry : sources.entrySet()) {
			// create repository
			FilterChainReferenceRepository referenceRepository = createFilterChainRepository(sourceEntry.getKey());

			// start chain
			for (FilterChainReference reference : referenceRepository.getAll()) {
				startFilterChain(sourceEntry.getKey(), sourceEntry.getValue(), reference);
			}
		}
	}


	/**
	 * Called once at lifecycle end.
	 */
	public void stopAllFilterChains() {
		executorService.shutdown();
	}


	private FilterChainReferenceRepository createFilterChainRepository(DataSource source) {
		FilterChainReferenceRepository referenceRepository = dbFactory.createFilterChainReferenceRepository(source.getSourceId());
		referenceRepositoryCache.put(source.getSourceId(), referenceRepository);
		return referenceRepository;
	}


	private void startFilterChain(DataSource source, DataRepository dataRepository, FilterChainReference reference) {
		FilterKey key = new FilterKey(source.getSourceId(), reference.getFilterChainId());
		ScheduledFuture<?> task = executorService.scheduleAtFixedRate(
				new FilterRunnable(reference, source, dataRepository),
				0,
				reference.getExecutionInterval().getPeriod(),
				reference.getExecutionInterval().getUnit());

		runningTasks.put(key, task);
	}


	private final class FilterRunnable implements Runnable {

		private final FilterChainReference reference;
		private final DataSource source;
		private final DataRepository dataRepository;

		public FilterRunnable(FilterChainReference reference, DataSource source, DataRepository dataRepository) {
			this.reference = reference;
			this.source = source;
			this.dataRepository = dataRepository;
		}


		@Override
		public void run() {
			try {
				Log.info("starting filter chain \"" + reference.getFilterChainId() + "\" for source \"" + source.getSourceId() + "\"");
				// TODO pass all arguments
				Filter<Void, ArrayNode> filterChain = filterChainFactory.createFilterChain(reference, source, dataRepository);
				filterChain.filter(null);
				Log.info("stopping filter chain \"" + reference.getFilterChainId() + "\" for source \"" + source.getSourceId() + "\"");
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
