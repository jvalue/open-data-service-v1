package org.jvalue.ods.rest.v1;


import com.google.inject.Inject;

import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.couchdb.data.DataSourceManager;
import org.jvalue.ods.processor.ProcessorChainManager;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/filterChains")
@Produces(MediaType.APPLICATION_JSON)
public final class ProcessorChainApi extends AbstractApi {

	// avoid executing filter chains faster than every second
	private static final EnumSet<TimeUnit> validExecutionIntervalUnits
			= EnumSet.of(TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS);

	private final DataSourceManager sourceManager;
	private final ProcessorChainManager chainManager;

	@Inject
	public ProcessorChainApi(
			DataSourceManager sourceManager,
			ProcessorChainManager chainManager) {

		this.sourceManager = sourceManager;
		this.chainManager = chainManager;
	}


	@GET
	public List<ProcessorReferenceChain> getAllProcessorChains(
			@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		return chainManager.getAll(source);
	}


	@GET
	@Path("/{filterChainId}")
	public ProcessorReferenceChain getProcessorChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		return chainManager.get(source, filterChainId);
	}


	@PUT
	@Path("/{filterChainId}")
	public ProcessorReferenceChain addProcessorChain(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId,
			@Valid ProcessorReferenceChainDescription processorChain) {

		if (processorChain.getExecutionInterval() != null) {
			assertIsValidTimeUnit(processorChain.getExecutionInterval().getUnit());
		}

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (chainManager.contains(source, filterChainId))
			throw RestUtils.createJsonFormattedException("filter chain with id " + filterChainId + " already exists", 409);

		ProcessorReferenceChain chainReference = new ProcessorReferenceChain(
				filterChainId,
				processorChain.getProcessors(),
				processorChain.getExecutionInterval());

		if (processorChain.getExecutionInterval() != null) chainManager.add(source, sourceManager.getDataRepository(source), chainReference);
		else chainManager.executeOnce(source, sourceManager.getDataRepository(source), chainReference);
		return chainReference;
	}


	@DELETE
	@Path("/{filterChainId}")
	public void deleteProcessorChain(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		ProcessorReferenceChain reference = chainManager.get(source, filterChainId);
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

}
