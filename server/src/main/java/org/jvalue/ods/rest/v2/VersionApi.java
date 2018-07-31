package org.jvalue.ods.rest.v2;


import org.jvalue.commons.rest.VersionInfo;
import org.jvalue.ods.GitConstants;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiMediaType;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.VersionInfoWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.jvalue.ods.rest.v2.AbstractApi.VERSION;

@Path(VERSION + "/version")
@Produces(JsonApiMediaType.JSONAPI)
public final class VersionApi {

	@Context private UriInfo uriInfo;

	private static final VersionInfo version = new VersionInfo(
			GitConstants.VERSION,
			"https://github.com/jvalue/open-data-service/commit/" +  GitConstants.COMMIT_HASH);


	@GET
	public Response getVersion() {
		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(VersionInfoWrapper.from(version))
			.build();
	}

}
