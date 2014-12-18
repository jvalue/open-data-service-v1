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
package org.jvalue.ods.processor;

import com.google.common.base.Objects;
import com.google.inject.Inject;

import org.jvalue.ods.data.AbstractDataSourcePropertyManager;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.ProcessorChainReferenceRepository;
import org.jvalue.ods.db.RepositoryCache;
import org.jvalue.ods.processor.reference.ProcessorChainReference;
import org.jvalue.ods.utils.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


public final class ProcessorChainManager extends AbstractDataSourcePropertyManager<ProcessorChainReference, ProcessorChainReferenceRepository> {

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private final Map<ProcessorKey, ScheduledFuture<?>> runningTasks = new HashMap<>();

	private final ProcessorChainFactory processorChainFactory;


	@Inject
	ProcessorChainManager(
			ProcessorChainFactory processorChainFactory,
			RepositoryCache<ProcessorChainReferenceRepository> referenceRepositoryCache,
			DbFactory dbFactory) {

		super(referenceRepositoryCache, dbFactory);
		this.processorChainFactory = processorChainFactory;
	}


	@Override
	protected void doAdd(DataSource source, DataRepository dataRepository, ProcessorChainReference reference) {
		startProcessorChain(source, dataRepository, reference);
	}


	@Override
	protected void doRemove(DataSource source, DataRepository dataRepository, ProcessorChainReference reference) {
		stopProcessorChain(source, reference);
	}


	@Override
	protected void doRemoveAll(DataSource source) {
		for (ProcessorChainReference reference : getAll(source)) stopProcessorChain(source, reference);
	}


	@Override
	protected ProcessorChainReference doGet(ProcessorChainReferenceRepository repository, String filterChainId) {
		return repository.findByProcessorChainId(filterChainId);
	}


	@Override
	protected ProcessorChainReferenceRepository createNewRepository(String sourceId, DbFactory dbFactory) {
		return dbFactory.createFilterChainReferenceRepository(sourceId);
	}



	/**
	 * Called once during lifecycle initialization.
	 * @param sources All sources including their data repositories to create the actual
	 *                filter chain and start them.
	 */
	public void startAllProcessorChains(Map<DataSource, DataRepository> sources) {
		for (Map.Entry<DataSource, DataRepository> sourceEntry : sources.entrySet()) {
			// start chain
			for (ProcessorChainReference reference : getAll(sourceEntry.getKey())) {
				startProcessorChain(sourceEntry.getKey(), sourceEntry.getValue(), reference);
			}
		}
	}


	/**
	 * Called once at lifecycle end.
	 */
	public void stopAllProcessorChains() {
		executorService.shutdown();
	}


	private void startProcessorChain(DataSource source, DataRepository dataRepository, ProcessorChainReference reference) {
		ProcessorKey key = new ProcessorKey(source.getSourceId(), reference.getProcessorChainId());
		ScheduledFuture<?> task = executorService.scheduleAtFixedRate(
				new ProcessorRunnable(reference, source, dataRepository),
				0,
				reference.getExecutionInterval().getPeriod(),
				reference.getExecutionInterval().getUnit());

		runningTasks.put(key, task);
	}


	private void stopProcessorChain(DataSource source, ProcessorChainReference reference) {
		ScheduledFuture<?> task = runningTasks.remove(new ProcessorKey(source.getSourceId(), reference.getProcessorChainId()));
		task.cancel(false);
	}


	private final class ProcessorRunnable implements Runnable {

		private final ProcessorChainReference reference;
		private final DataSource source;
		private final DataRepository dataRepository;

		public ProcessorRunnable(ProcessorChainReference reference, DataSource source, DataRepository dataRepository) {
			this.reference = reference;
			this.source = source;
			this.dataRepository = dataRepository;
		}


		@Override
		public void run() {
			try {
				Log.info("starting processor chain \"" + reference.getProcessorChainId() + "\" for source \"" + source.getSourceId() + "\"");
				ProcessorChain chain = processorChainFactory.createProcessorChain(reference, source, dataRepository);
				chain.startProcessing();
				Log.info("stopping processor chain \"" + reference.getProcessorChainId() + "\" for source \"" + source.getSourceId() + "\"");
			} catch (Throwable throwable) {
				Log.error("error while running processor chain", throwable);
			}
		}

	}


	private static final class ProcessorKey {
		private final String dataSourceId, processorChainId;

		public ProcessorKey(String dataSourceId, String processorChainId) {
			this.dataSourceId = dataSourceId;
			this.processorChainId = processorChainId;
		}


		@Override
		public boolean equals(Object other) {
			if (other == null || !(other instanceof ProcessorKey)) return false;
			if (other == this) return true;
			ProcessorKey key = (ProcessorKey) other;
			return Objects.equal(dataSourceId, key.dataSourceId)
					&& Objects.equal(processorChainId, key.processorChainId);
		}


		@Override
		public int hashCode() {
			return Objects.hashCode(dataSourceId, processorChainId);
		}

	}
}
