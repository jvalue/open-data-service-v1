package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.filter.description.FilterDescription;
import org.jvalue.ods.filter.description.FilterDescriptionManager;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/filterTypes")
@Produces(MediaType.APPLICATION_JSON)
public final class FilterDescriptionApi extends AbstractApi {

	private final FilterDescriptionManager descriptionManager;

	@Inject
	public FilterDescriptionApi(FilterDescriptionManager descriptionManager) {
		this.descriptionManager = descriptionManager;
	}


	@GET
	public List<FilterDescription> getAll() {
		return new LinkedList<>(descriptionManager.getAll());
	}
}
