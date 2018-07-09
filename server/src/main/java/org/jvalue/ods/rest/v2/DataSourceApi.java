package org.jvalue.ods.rest.v2;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.rest.v2.jsonApi.JsonApiResponse;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(AbstractApi.BASE_URL)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceManager sourceManager;
    @Context private UriInfo uriInfo;

    @Inject
	public DataSourceApi(DataSourceManager sourceManager) {
		this.sourceManager = sourceManager;
	}


    @GET
    public Response getAllSources() {
        return JsonApiResponse
                .uriInfo(uriInfo)
                .ok()
                .entity(sourceManager.getAll())
                .build();
    }


    @GET
    @Path("/{sourceId}")
    public Response getSource(@PathParam("sourceId") String sourceId) {

        DataSource source = sourceManager.findBySourceId(sourceId);

        return JsonApiResponse
                .uriInfo(uriInfo)
                .ok()
                .entity(source)
                .build();
    }


    @GET
    @Path("/{sourceId}/schema")
    public Response getSourceSchema(@PathParam("sourceId") String sourceId) {

        String id = sourceId+"_schema";
        JsonNode schema = sourceManager.findBySourceId(sourceId).getSchema();

//        return new JsonApiResponse()
//                .uriInfo(uriInfo)
//                .ok()
//                .entity(schema)
//                .build();

        return JsonApiResponse.no_content().build();
    }


    @PUT
    @Path("/{sourceId}")
    public Response addSource(
            @RestrictedTo(Role.ADMIN) User user,
            @PathParam("sourceId") String sourceId,
            @Valid DataSourceDescription sourceDescription) {

        DataSource source = new DataSource(
                sourceId,
                sourceDescription.getDomainIdKey(),
                sourceDescription.getSchema(),
                sourceDescription.getMetaData());
        Response response;

        if(sourceManager.isValidSourceId(sourceId)) {
            sourceManager.remove(sourceManager.findBySourceId(sourceId));
            response = JsonApiResponse
                    .uriInfo(uriInfo)
                    .ok()
                    .entity(source)
                    .build();
        }
        else {
            response = JsonApiResponse
                    .uriInfo(uriInfo)
                    .created()
                    .entityIdentifier(source)
                    .build();
        }
        sourceManager.add(source);

        return response;
	}


	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		sourceManager.remove(sourceManager.findBySourceId(sourceId));
	}

}
