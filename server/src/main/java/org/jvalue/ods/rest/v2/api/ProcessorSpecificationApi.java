package org.jvalue.ods.rest.v2.api;


import com.google.inject.Inject;
import org.jvalue.ods.api.processors.Specification;
import org.jvalue.ods.processor.specification.SpecificationManager;
import org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse;
import org.jvalue.ods.rest.v2.jsonapi.wrapper.SpecificationWrapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

import static org.jvalue.ods.rest.v2.jsonapi.response.JsonApiResponse.JSONAPI_TYPE;

@Path(AbstractApi.VERSION + "/filterTypes")
@Produces(JSONAPI_TYPE)
public final class ProcessorSpecificationApi extends AbstractApi {

	private final SpecificationManager descriptionManager;

	@Context
	private UriInfo uriInfo;

	@Inject
	public ProcessorSpecificationApi(SpecificationManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	@GET
	public Response getAllSpecifications() {
		Set<Specification> specs = descriptionManager.getAll();

		return JsonApiResponse
			.createGetResponse(uriInfo)
			.data(SpecificationWrapper.fromCollection(specs))
			.build();
	}
}
