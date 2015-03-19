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

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Objects;
import com.google.inject.Inject;

import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.AbstractDataSourcePropertyManager;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.db.ProcessorChainReferenceRepository;
import org.jvalue.common.utils.Assert;
import org.jvalue.ods.utils.Cache;
import org.jvalue.ods.utils.ListValueMap;
import org.jvalue.common.utils.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


public final class ProcessorChainManager extends AbstractDataSourcePropertyManager<ProcessorReferenceChain, ProcessorChainReferenceRepository> {

	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private final Map<ProcessorKey, ScheduledFuture<?>> scheduledTasks = new HashMap<>();				// reoccurring tasks to stop if necessary
	private final ListValueMap<DataSource, ProcessorReferenceChain> runningTasks = new ListValueMap<>();	// all running tasks
	private final Timer processorTimer;

	private final ProcessorChainFactory processorChainFactory;


	@Inject
	ProcessorChainManager(
			ProcessorChainFactory processorChainFactory,
			Cache<ProcessorChainReferenceRepository> referenceRepositoryCache,
			RepositoryFactory repositoryFactory,
			MetricRegistry registry) {

		super(referenceRepositoryCache, repositoryFactory);
		this.processorChainFactory = processorChainFactory;
		this.processorTimer = registry.timer(MetricRegistry.name(ProcessorChainManager.class, "processing-total"));
		registry.register(MetricRegistry.name(ProcessorChainManager.class, "running-tasks"), new Gauge<Integer>() {
			@Override
			public Integer getValue() {
				return runningTasks.size();
			}
		});

	}


	public void executeOnce(DataSource source, DataRepository dataRepository, ProcessorReferenceChain reference) {
		Assert.assertTrue(reference.getExecutionInterval() == null, "reference must not contain an execute interval");
		startProcessorChain(source, dataRepository, reference);
	}


	@Override
	protected void doAdd(DataSource source, DataRepository dataRepository, ProcessorReferenceChain reference) {
		Assert.assertNotNull(reference.getExecutionInterval());
		startProcessorChain(source, dataRepository, reference);
	}


	@Override
	protected void doRemove(DataSource source, DataRepository dataRepository, ProcessorReferenceChain reference) {
		stopProcessorChain(source, reference);
	}


	@Override
	protected void doRemoveAll(DataSource source) {
		for (ProcessorReferenceChain reference : getAll(source)) stopProcessorChain(source, reference);
	}


	@Override
	protected ProcessorChainReferenceRepository createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createFilterChainReferenceRepository(sourceId);
	}


	/**
	 * Called once during lifecycle initialization.
	 * @param sources All sources including their data repositories to create the actual
	 *                filter chain and start them.
	 */
	public void startAllProcessorChains(Map<DataSource, DataRepository> sources) {
		for (Map.Entry<DataSource, DataRepository> sourceEntry : sources.entrySet()) {
			// start chain
			for (ProcessorReferenceChain reference : getAll(sourceEntry.getKey())) {
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


	public ListValueMap<DataSource, ProcessorReferenceChain> getAllRunningTasks() {
		return runningTasks;
	}


	private void startProcessorChain(DataSource source, DataRepository dataRepository, ProcessorReferenceChain reference) {
		ProcessorKey key = new ProcessorKey(source.getId(), reference.getId());

		Runnable runnable = new ProcessorRunnable(reference, source, dataRepository);
		if (reference.getExecutionInterval() != null) {
			System.out.println("inserting key (" + key.hashCode() + ")");
			ScheduledFuture<?> task = executorService.scheduleAtFixedRate(
					runnable,
					0,
					reference.getExecutionInterval().getPeriod(),
					reference.getExecutionInterval().getUnit());
			scheduledTasks.put(key, task);
		} else {
			executorService.execute(runnable);
		}
	}


	private void stopProcessorChain(DataSource source, ProcessorReferenceChain reference) {
		ProcessorKey key = new ProcessorKey(source.getId(), reference.getId());
		System.out.println("removing key (" + key.hashCode() + ")");
		ScheduledFuture<?> task = scheduledTasks.remove(key);
		task.cancel(false);
		runningTasks.remove(source, reference);
	}


	private final class ProcessorRunnable implements Runnable {

		private final ProcessorReferenceChain reference;
		private final DataSource source;
		private final DataRepository dataRepository;

		public ProcessorRunnable(ProcessorReferenceChain reference, DataSource source, DataRepository dataRepository) {
			this.reference = reference;
			this.source = source;
			this.dataRepository = dataRepository;
		}


		@Override
		public void run() {
			Log.info("starting processor chain \"" + reference.getId() + "\" for source \"" + source.getId() + "\"");
			runningTasks.add(source, reference);
			Timer.Context timerContext = processorTimer.time();
			try {
				processorChainFactory.createProcessorChain(reference, source, dataRepository).startProcessing();
			} catch (Throwable throwable) {
				Log.error("error while running processor chain", throwable);
			} finally {
				timerContext.stop();
				runningTasks.remove(source, reference);
				Log.info("stopping processor chain \"" + reference.getId() + "\" for source \"" + source.getId() + "\"");
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
