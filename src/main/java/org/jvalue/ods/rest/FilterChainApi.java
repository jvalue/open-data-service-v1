package org.jvalue.ods.rest;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.processor.reference.ExecutionInterval;
import org.jvalue.ods.processor.reference.ProcessorChainReference;
import org.jvalue.ods.processor.reference.ProcessorReference;
import org.jvalue.ods.processor.reference.ProcessorReferenceFactory;
import org.jvalue.ods.utils.Assert;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/filterChains")
@Produces(MediaType.APPLICATION_JSON)
public final class FilterChainApi extends AbstractApi {

	// avoid executing filter chains faster than every second
	private static final EnumSet<TimeUnit> validExecutionIntervalUnits
			= EnumSet.of(TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS);

	private final DataSourceManager sourceManager;
	private final ProcessorChainManager chainManager;
	private final ProcessorReferenceFactory referenceFactory;

	@Inject
	public FilterChainApi(
			DataSourceManager sourceManager,
			ProcessorChainManager chainManager,
			ProcessorReferenceFactory referenceFactory) {

		this.sourceManager = sourceManager;
		this.chainManager = chainManager;
		this.referenceFactory = referenceFactory;
	}


	@GET
	public List<ProcessorChainReference> getAllFilterChains(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		return chainManager.getAll(source);
	}


	@GET
	@Path("/{filterChainId}")
	public ProcessorChainReference getSingleFilterChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		return chainManager.get(source, filterChainId);
	}


	@PUT
	@Path("/{filterChainId}")
	public ProcessorChainReference addFilterChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId,
			FilterChain filterChain) {

		assertIsValidTimeUnit(filterChain.executionInterval.getUnit());
		assertIsValidPeriod(filterChain.executionInterval.getPeriod());

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (chainManager.contains(source, filterChainId))
			throw RestUtils.createJsonFormattedException("filter chain with id " + filterChainId + " already exists", 409);

		try {
			List<ProcessorReference> processorReferences = new LinkedList<>();
			for (Filter filter : filterChain.filters) {
				ProcessorReference reference = referenceFactory.createProcessorReference(filter.name, filter.arguments);
				processorReferences.add(reference);
			}

			ProcessorChainReference chainReference = referenceFactory.createProcessorChainReference(
					filterChainId,
					processorReferences,
					filterChain.executionInterval);

			chainManager.add(source, sourceManager.getDataRepository(source), chainReference);
			return chainReference;
		} catch (ProcessorReferenceFactory.InvalidProcessorException ife) {
			throw RestUtils.createJsonFormattedException(ife.getMessage(), 400);
		}
	}


	@DELETE
	@Path("/{filterChainId}")
	public void removeFilterChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		ProcessorChainReference reference = chainManager.get(source, filterChainId);
		chainManager.remove(source, sourceManager.getDataRepository(source), reference);
	}


	private void assertIsValidTimeUnit(TimeUnit unit) {
		if (!validExecutionIntervalUnits.contains(unit)) {
			StringBuilder builder = new StringBuilder();
			builder.append("time unit must be one of: ");
			boolean firstIter = true;
			for (TimeUnit validUnit : validExecutionIntervalUnits) {
				if (!firstIter) builder.append(", ");
				else firstIter = false;
				builder.append(validUnit.toString());
			}
			throw RestUtils.createJsonFormattedException(builder.toString(), 400);
		}
	}


	private void assertIsValidPeriod(long period) {
		if (period <= 0) throw RestUtils.createJsonFormattedException("period must be > 0", 400);
	}


	private static final class FilterChain {

		private final List<Filter> filters;
		private final ExecutionInterval executionInterval;

		@JsonCreator
		public FilterChain(
				@JsonProperty("processors") List<Filter> processors,
				@JsonProperty("executionInterval") ExecutionInterval executionInterval) {

			Assert.assertNotNull(processors, executionInterval);
			this.filters = processors;
			this.executionInterval = executionInterval;
		}

	}


	private static final class Filter {

		private final String name;
		private final Map<String, Object> arguments;

		@JsonCreator
		public Filter(
				@JsonProperty("name") String name,
				@JsonProperty("arguments") Map<String, Object> arguments) {

			Assert.assertNotNull(name);
			this.name = name;
			if (arguments == null) this.arguments = new HashMap<>();
			else this.arguments = arguments;
		}

	}
}
