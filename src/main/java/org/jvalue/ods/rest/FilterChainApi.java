package org.jvalue.ods.rest;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceManager;
import org.jvalue.ods.filter.FilterChainManager;
import org.jvalue.ods.filter.reference.FilterChainMetaData;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.filter.reference.FilterReference;
import org.jvalue.ods.filter.reference.FilterReferenceManager;
import org.jvalue.ods.utils.Assert;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/filterChains")
@Produces(MediaType.APPLICATION_JSON)
public final class FilterChainApi extends AbstractApi {

	private final DataSourceManager sourceManager;
	private final FilterChainManager chainManager;
	private final FilterReferenceManager referenceManager;

	@Inject
	public FilterChainApi(
			DataSourceManager sourceManager,
			FilterChainManager chainManager,
			FilterReferenceManager referenceManager) {

		this.sourceManager = sourceManager;
		this.chainManager = chainManager;
		this.referenceManager = referenceManager;
	}


	@GET
	public List<FilterChainReference> getAllFilterChains(@PathParam("sourceId") String sourceId) {
		DataSource source = sourceManager.findBySourceId(sourceId);
		return chainManager.getAllForSource(source);
	}


	@GET
	@Path("/{filterChainId}")
	public FilterChainReference getSingleFilterChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		return chainManager.get(source, filterChainId);
	}


	@PUT
	@Path("/{filterChainId}")
	public FilterChainReference addFilterChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("filterChainId") String filterChainId,
			FilterChainReferenceDescription description) {

		DataSource source = sourceManager.findBySourceId(sourceId);
		if (chainManager.filterChainExists(source, filterChainId))
			throw RestUtils.createJsonFormattedException("filter chain with id " + filterChainId + " already exists", 409);

		List<FilterReference> filterReferences = new LinkedList<>();
		for (String filterName : description.filterNames) {
			FilterReference reference = referenceManager.getFilterReferenceByName(filterName);
			if (reference == null) {
				throw RestUtils.createJsonFormattedException("unknown filter '" + filterName + "'", 400);
			}
			filterReferences.add(reference);
		}

		try {
			FilterChainReference chainReference = referenceManager.createFilterChainReference(
					filterChainId,
					filterReferences,
					new FilterChainMetaData(-1));

			chainManager.add(source, sourceManager.getDataRepository(source), chainReference);
			return chainReference;
		} catch (FilterReferenceManager.InvalidFilterReferenceListException ifre) {
			throw RestUtils.createJsonFormattedException(ifre.getMessage(), 400);
		}
	}


	private static final class FilterChainReferenceDescription {

		private final List<String> filterNames;

		public FilterChainReferenceDescription(
				@JsonProperty("filterNames") List<String> filterNames) {

			Assert.assertNotNull(filterNames);
			this.filterNames = filterNames;
		}

	}
}
