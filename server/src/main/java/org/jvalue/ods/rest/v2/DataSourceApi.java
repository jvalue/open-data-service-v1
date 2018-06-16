package org.jvalue.ods.rest.v2;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.data.DataSourceManager;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(AbstractApi.BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;

	@Inject
	public DataSourceApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}


	@GET
	public JsonApiCollection<DataSource> getAllSources() {
	    return new JsonApiCollection<>(sourceManager.getAll(), BASE_URL);
	}


	//todo: integrate links to next and last dataSource. are these datasources ordered?
	@GET
	@Path("/{sourceId}")
	public JsonApiIndividual<DataSource> getSource(@PathParam("sourceId") String sourceId) {
	    DataSource source = sourceManager.findBySourceId(sourceId);
        return new JsonApiIndividual<>(source, BASE_URL+"/"+sourceId);
	}


	@GET
	@Path("/{sourceId}/schema")
	public JsonApiIndividual<JsonNode> getSourceSchema(@PathParam("sourceId") String sourceId) {
	    String id = sourceId+"schema";
		return new JsonApiIndividual<>(sourceManager.findBySourceId(sourceId).getSchema(), BASE_URL+"/"+sourceId+"/schema", "schema", id);
	}

    @PUT
    @Path("/{sourceId}")
    public Response addSource(
            @RestrictedTo(Role.ADMIN) User user,
            @PathParam("sourceId") String sourceId,
            @Valid DataSourceDescription sourceDescription) {


        // http1.1 spec sagt:
        // If the Request-URI refers to an already existing resource,
        // the enclosed entity SHOULD be considered as a modified version of the one residing on the origin server.
        // statuscode 200 oder 204 wenn already exists
        int statusCode = 201;
        if(sourceManager.isValidSourceId(sourceId)) {
            sourceManager.remove(sourceManager.findBySourceId(sourceId));
            statusCode = 200;
        }

        DataSource source = new DataSource(
                sourceId,
                sourceDescription.getDomainIdKey(),
                sourceDescription.getSchema(),
                sourceDescription.getMetaData());
        sourceManager.add(source);
        return Response.status(Response.Status.fromStatusCode(statusCode)).entity(new JsonApiIndividual<>(source, BASE_URL+"/"+sourceId)).build();
    }

	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		sourceManager.remove(sourceManager.findBySourceId(sourceId));
	}

}
