package org.jvalue.ods.rest.v2.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.commons.rest.VersionInfo;
import org.jvalue.ods.GitConstants;
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

	private static final VersionInfo version = new VersionInfo(
		GitConstants.VERSION,
		"https://github.com/jvalue/open-data-service/commit/" + GitConstants.COMMIT_HASH);


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
			.build();
	}

	private class EntryPointData implements JsonApiIdentifiable{


		@Override
		public String getId() {
			return "jValue_OpenDataService";
		}

		@Override
		public String getType() {
			return "EntryPoint";
		}

		@JsonProperty("API_version")
		public String getApiVersion() {
			return API_VERSION;
		}

		@JsonProperty("ODS_version")
		public VersionInfo getOdsVersion() {
			return version;
		}
	}
}
