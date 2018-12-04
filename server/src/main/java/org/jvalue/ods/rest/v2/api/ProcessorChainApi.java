package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.processor.ProcessorChainManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiRequest;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonLinks;
import org.jvalue.ods.rest.v2.jsonapi.swagger.JsonApiSchema;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.ProcessorReferenceChainWrapper;
import org.jvalue.ods.utils.JsonMapper;
import org.jvalue.ods.utils.RequestValidator;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.jvalue.ods.rest.v2.api.AbstractApi.BASE_URL;
import static org.jvalue.ods.rest.v2.api.AbstractApi.FILTERCHAINS;
import static org.jvalue.ods.utils.HttpUtils.getDirectoryURI;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;

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


	@Operation(
		operationId = FILTERCHAINS,
		tags = FILTERCHAINS,
		summary = "Get all filterchains",
		description = "Get all filterchains registered for a datasource"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		content = @Content(schema = @Schema(implementation = JsonApiSchema.ProcessorReferenceChainSchema.class)),
		links = @Link(name = DATASOURCE, operationRef = DATASOURCE)
	)
	@ApiResponse(
		responseCode = "404", description = "Datasource not found"
	)
	@GET
	public Response getAllProcessorChains(
		@PathParam("sourceId") @Parameter(description = "Id of the datasource")
			String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		List<ProcessorReferenceChain> chains = chainManager.getAll(source);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ProcessorReferenceChainWrapper.fromCollection(chains))
			.addLink(DATASOURCE, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = FILTERCHAINS,
		summary = "Get a filterchain",
		description = "Get a filterchain for a datasource by its id"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Ok",
		links = @Link(name = FILTERCHAINS, operationRef = FILTERCHAINS),
		content = @Content(schema = @Schema(implementation = JsonApiSchema.ProcessorReferenceChainSchema.class))
	)
	@ApiResponse(
		responseCode = "404", description = "Filterchain or datasource not found"
	)
	@GET
	@Path("/{filterChainId}")
	public Response getProcessorChain(
		@PathParam("sourceId") @Parameter(description = "Id of the datasource")
			String sourceId,
		@PathParam("filterChainId") @Parameter(description = "Id of the filterchain")
			String filterChainId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		ProcessorReferenceChain chain = chainManager.get(source, filterChainId);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ProcessorReferenceChainWrapper.from(chain))
			.addLink(FILTERCHAINS, getDirectoryURI(uriInfo))
			.build();
	}


	@Operation(
		tags = FILTERCHAINS,
		summary = "Add a filterchain",
		description = "Add a filterchain to a datasource"
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "201",
		description = "filterchain added",
		links = @Link(name = FILTERCHAINS, operationRef = FILTERCHAINS),
		content = @Content(schema = @Schema(implementation = JsonApiSchema.ProcessorReferenceChainSchema.class))
	)
	@ApiResponse(
		responseCode = "401", description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "401", description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404", description = "Datasource not found"
	)
	@ApiResponse(
		responseCode = "409", description = "Filterchain with given id already exists"
	)
	@POST
	public Response addProcessorChain(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@Valid @RequestBody(content = @Content(schema = @Schema(implementation = JsonApiSchema.ProcessorReferenceChainSchema.class)))
			JsonApiRequest processorChainRequest) {

		ProcessorReferenceChainDescription processorChain = JsonMapper.convertValue(
			processorChainRequest.getAttributes(),
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

		URI directoryURI = getSanitizedPath(uriInfo);

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(ProcessorReferenceChainWrapper.from(chainReference))
			.addLink(JsonLinks.SELF, directoryURI.resolve(processorChainRequest.getId()))
			.addLink(FILTERCHAINS, directoryURI)
			.build();
	}


	@Operation(
		tags = FILTERCHAINS,
		summary = "Delete filterchain",
		description = "Delete a filterchain from a datasource"
	)
	@SecurityRequirement(name = BASICAUTH)
	@ApiResponse(
		responseCode = "200", description = "Filterchain deleted"
	)
	@ApiResponse(
		responseCode = "401", description = "Not authorized"
	)
	@ApiResponse(
		responseCode = "404", description = "Filterchain or datasource not found"
	)
	@DELETE
	@Path("/{filterChainId}")
	public void deleteProcessorChain(
		@RestrictedTo(Role.ADMIN) @Parameter(hidden = true)
			User user,
		@PathParam("sourceId") @Parameter(description = "Id of the source from which the filterchain shall be deleted")
			String sourceId,
		@PathParam("filterChainId") @Parameter(description = "Id of the filterchain to be deleted")
			String filterChainId) {

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
			throw createJsonApiException("filter chain with id " + id + " already exists", Response.Status.CONFLICT);
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
			throw createJsonApiException(builder.toString(), Response.Status.BAD_REQUEST);
		}
	}

}
