package org.jvalue.ods.rest.v1;


import com.google.inject.Inject;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.generic.TransformationFunctionDescription;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.data.DataTransformationManager;
import org.jvalue.ods.api.views.generic.TransformationFunction;

import javax.script.ScriptException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path(AbstractApi.BASE_URL + "/{sourceId}/transformations")
@Produces(MediaType.APPLICATION_JSON)
public final class DataTransformationApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final DataTransformationManager dataTransformationManager;


	@Inject
	public DataTransformationApi(
		DataSourceManager sourceManager,
		DataTransformationManager dataTransformationManager) {

		this.sourceManager = sourceManager;
		this.dataTransformationManager = dataTransformationManager;
	}


	@GET
	public List<TransformationFunction> getAllViews(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		return dataTransformationManager.getAll(source);
	}


	@GET
	@Path("/{viewId}")
	public Object getView(
		@PathParam("sourceId") String sourceId,
		@PathParam("viewId") String viewId,
		@QueryParam("execute") boolean execute,
		@QueryParam("argument") String argument) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		TransformationFunction transformationFunction = dataTransformationManager.get(source, viewId);

		if (!execute) return transformationFunction;
		try {
			return dataTransformationManager.transformAndReduce(sourceManager.getDataRepository(source), transformationFunction);
		} catch (ScriptException | NoSuchMethodException e) {
			throw RestUtils.createJsonFormattedException("Script execution error: "+ e.getMessage(), 500);
		} catch (IOException e) {
			throw RestUtils.createJsonFormattedException("Script execution error: The return value of transformation function is not a valid JSON.", 500);
		}
	}


	@PUT
	@Path("/{viewId}")
	public TransformationFunction addView(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@PathParam("viewId") String viewId,
		@Valid TransformationFunctionDescription viewDescription) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (dataTransformationManager.contains(source, viewId))
			throw RestUtils.createJsonFormattedException("transformation view with id " + viewId + " already exists", 409);

		TransformationFunction transformationFunction = new TransformationFunction(viewId, viewDescription.getTransformationFunction(), viewDescription.getReduceFunction());


		dataTransformationManager.add(source, sourceManager.getDataRepository(source), transformationFunction);
		return transformationFunction;
	}


	@DELETE
	@Path("/{viewId}")
	public void deleteView(
		@RestrictedTo(Role.ADMIN) User user,
		@PathParam("sourceId") String sourceId,
		@PathParam("viewId") String viewId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		TransformationFunction view = dataTransformationManager.get(source, viewId);
		dataTransformationManager.remove(source, sourceManager.getDataRepository(source), view);
	}

}
