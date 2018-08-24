package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.ProcessorReferenceChainWrapper;
import org.jvalue.ods.utils.JsonMapper;
import org.jvalue.ods.utils.RequestValidator;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.jvalue.ods.rest.v2.api.AbstractApi.BASE_URL;
import static org.jvalue.ods.rest.v2.api.AbstractApi.FILTERCHAINS;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;

@Path(BASE_URL + "/{sourceId}/" + FILTERCHAINS)
public final class ProcessorChainApi extends AbstractApi {

	// avoid executing filter chains faster than every second
	private static final EnumSet<TimeUnit> validExecutionIntervalUnits
		= EnumSet.of(TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS);

	private final DataSourceManager sourceManager;
	private final ProcessorChainManager chainManager;

	@Context
	private UriInfo uriInfo;

	@Inject
	public ProcessorChainApi(
		DataSourceManager sourceManager,
		ProcessorChainManager chainManager) {

		this.sourceManager = sourceManager;
		this.chainManager = chainManager;
	}


	@GET
	public Response getAllProcessorChains(
		@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		List<ProcessorReferenceChain> chains = chainManager.getAll(source);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ProcessorReferenceChainWrapper.fromCollection(chains))
			.addLink("source", getDirectoryURI(uriInfo))
			.build();
	}


	@GET
	@Path("/{filterChainId}")
	public Response getProcessorChain(
		@PathParam("sourceId") String sourceId,
		@PathParam("filterChainId") String filterChainId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		ProcessorReferenceChain chain = chainManager.get(source, filterChainId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ProcessorReferenceChainWrapper.from(chain))
			.addLink(FILTERCHAINS, getDirectoryURI(uriInfo))
			.build();
	}


	@POST
	public Response addProcessorChain(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@Valid JsonApiRequest processorChainRequest) {

		ProcessorReferenceChainDescription processorChain = JsonMapper.convertValue(
			processorChainRequest,
			ProcessorReferenceChainDescription.class
		);

		DataSource source = sourceManager.findBySourceId(sourceId);

		assertIsValidProcessorChainDescription(processorChain, processorChainRequest.getId(), source);

		ProcessorReferenceChain chainReference = new ProcessorReferenceChain(
			processorChainRequest.getId(),
			processorChain.getProcessors(),
			processorChain.getExecutionInterval());

		if (processorChain.getExecutionInterval() != null) {
			chainManager.add(source, sourceManager.getDataRepository(source), chainReference);
		} else {
			chainManager.executeOnce(source, sourceManager.getDataRepository(source), chainReference);
		}

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ProcessorReferenceChainWrapper.from(chainReference))
			.addLink(FILTERCHAINS, getDirectoryURI(uriInfo))
			.build();
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


	private void assertIsValidProcessorChainDescription(ProcessorReferenceChainDescription description, String id, DataSource source) {
		RequestValidator.validate(description);

		if (description.getExecutionInterval() != null) {
			assertIsValidTimeUnit(description.getExecutionInterval().getUnit());
		}

		if (chainManager.contains(source, id))
			throw RestUtils.createJsonFormattedException("filter chain with id " + id + " already exists", 409);
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
