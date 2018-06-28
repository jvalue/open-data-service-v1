package org.jvalue.ods.rest.v1;


import com.google.inject.Inject;

import org.jvalue.ods.api.processors.Specification;
import org.jvalue.ods.processor.specification.SpecificationManager;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.VERSION + "/filterTypes")
@Produces(MediaType.APPLICATION_JSON)
public final class ProcessorSpecificationApi extends AbstractApi {

	private final SpecificationManager descriptionManager;

	@Inject
	public ProcessorSpecificationApi(SpecificationManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	@GET
	public List<Specification> getAllSpecifications() {
		return new LinkedList<>(descriptionManager.getAll());
	}
}
