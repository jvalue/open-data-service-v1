package org.jvalue.ods.rest.v2.api;

import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.JsonApiIdentifiable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.jvalue.ods.rest.v2.api.AbstractApi.V2;
import static org.jvalue.ods.utils.HttpUtils.getSanitizedPath;

@Path(V2)
public class EntryPoint extends AbstractApi {

	private final EntryPointData data = new EntryPointData();

	@Context private UriInfo uriInfo;

	@GET
	public Response getApiInfos() {
		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(data)
			.addLink(DATASOURCES, getSanitizedPath(uriInfo).resolve(DATASOURCES))
			.addLink(FILTERTYPES, getSanitizedPath(uriInfo).resolve(FILTERTYPES))
			.addLink(USERS, getSanitizedPath(uriInfo).resolve(USERS))
			.addLink(VERSION, getSanitizedPath(uriInfo).resolve(VERSION))
			.build();
	}

	private class EntryPointData implements JsonApiIdentifiable{

		@Override
		public String getId() {
			return "jValueODS_API_V2";
		}

		@Override
		public String getType() {
			return "EntryPoint";
		}
	}
}
