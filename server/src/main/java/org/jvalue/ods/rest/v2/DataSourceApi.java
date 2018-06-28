package org.jvalue.ods.rest.v2;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.rest.v2.JsonApi.JsonApiResponse;
import org.jvalue.ods.rest.v2.JsonApi.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(AbstractApi.BASE_URL)
@Produces(MediaType.JSONAPI)
@Consumes({MediaType.APPLICATION_JSON, MediaType.JSONAPI}) //todo: remove APPLICATION_JSON or keep it to retain downward compatibility?
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;

	@Inject
	public DataSourceApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}

    @Context UriInfo uriInfo;
    @Context
    HttpServletRequest servletRequest;
    @Context
    ServletContext servletContext;

    @GET
    public Response getAllSources() {
        return new JsonApiResponse<DataSource>()
                .ok()
                .path(uriInfo.getAbsolutePath())
                .entity(sourceManager.getAll())
                .build();
    }

    //todo: integrate links to next and last dataSource. are these datasources ordered?
    @GET
    @Path("/{sourceId}")
    public Response getSource(@PathParam("sourceId") String sourceId) {
        DataSource source = sourceManager.findBySourceId(sourceId);
        return new JsonApiResponse<DataSource>()
                .ok()
                .path(uriInfo.getAbsolutePath())
                .entity(source)
                .build();
    }

    @GET
    @Path("/{sourceId}/schema")
    public Response getSourceSchema(@PathParam("sourceId") String sourceId) {
        String id = sourceId+"_schema";
        JsonNode schema = sourceManager.findBySourceId(sourceId).getSchema();
        return new JsonApiResponse<JsonNode>()
                .ok()
                .path(uriInfo.getAbsolutePath())
                .entity(schema, id)
                .build();
    }

    @PUT
    @Path("/{sourceId}")
    public Response addSource(
            @RestrictedTo(Role.ADMIN) User user,
            @PathParam("sourceId") String sourceId,
            @Valid DataSourceDescription sourceDescription) {

        //todo: was tun, wenn request im jsonApi Format kommt??


        // http1.1 spec sagt:
        // If the Request-URI refers to an already existing resource,
        // the enclosed entity SHOULD be considered as a modified version of the one residing on the origin server.
        // statuscode 200 oder 204 wenn already exists
        Response.StatusType status = Response.Status.CREATED;
        if(sourceManager.isValidSourceId(sourceId)) {
            sourceManager.remove(sourceManager.findBySourceId(sourceId));
            status = Response.Status.OK;
        }

        DataSource source = new DataSource(
                sourceId,
                sourceDescription.getDomainIdKey(),
                sourceDescription.getSchema(),
                sourceDescription.getMetaData());
        sourceManager.add(source);

        return new JsonApiResponse<DataSource>()
                .status(status)
                .path(uriInfo.getAbsolutePath())
                .entity(source)
                .build();
	}

	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		sourceManager.remove(sourceManager.findBySourceId(sourceId));
	}

}