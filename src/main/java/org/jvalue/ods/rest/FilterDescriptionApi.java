package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.processor.specification.Specification;
import org.jvalue.ods.processor.specification.SpecificationManager;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/filterTypes")
@Produces(MediaType.APPLICATION_JSON)
public final class FilterDescriptionApi extends AbstractApi {

	private final SpecificationManager descriptionManager;

	@Inject
	public FilterDescriptionApi(SpecificationManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	@GET
	public List<Specification> getAll() {
		return new LinkedList<>(descriptionManager.getAll());
	}
}
