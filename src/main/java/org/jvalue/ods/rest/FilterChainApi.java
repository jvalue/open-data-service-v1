package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.db.FilterChainReferenceRepository;
import org.jvalue.ods.filter.reference.FilterChainMetaData;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.filter.reference.FilterReference;
import org.jvalue.ods.filter.reference.FilterReferenceManager;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/filterChains")
@Produces(MediaType.APPLICATION_JSON)
public final class FilterChainApi extends AbstractApi {

	private final DataSourceRepository sourceRepository;
	private final FilterChainReferenceRepository referenceRepository;
	private final FilterReferenceManager referenceManager;

	@Inject
	public FilterChainApi(
			DataSourceRepository sourceRepository,
			FilterChainReferenceRepository referenceRepository,
			FilterReferenceManager referenceManager) {

		this.sourceRepository = sourceRepository;
		this.referenceRepository = referenceRepository;
		this.referenceManager = referenceManager;
	}


	@GET
	public List<FilterChainReference> getAllFilterChains(@PathParam("sourceId") String sourceId) {
		sourceRepository.findBySourceId(sourceId);
		return referenceRepository.findByDataSourceId(sourceId);
	}


	@GET
	@Path("/{chainId}")
	public FilterChainReference getSingleFilterChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("chainId") String chainId) {

		sourceRepository.findBySourceId(sourceId);
		return referenceRepository.get(chainId);
	}


	@POST
	public FilterChainReference addFilterChain(
			@PathParam("sourceId") String sourceId,
			List<String> filterNames) {

		DataSource source = sourceRepository.findBySourceId(sourceId);
		List<FilterReference> filterReferences = new LinkedList<>();
		for (String filterName : filterNames) {
			FilterReference reference = referenceManager.getFilterReferenceByName(filterName);
			if (reference == null) {
				throw RestUtils.createJsonFormattedException("unknown filter '" + filterName + "'", 400);
			}
			filterReferences.add(reference);
		}

		try {
			FilterChainReference chainReference = referenceManager.createFilterChainReference(
					source.getSourceId(),
					filterReferences,
					new FilterChainMetaData(-1));

			referenceRepository.add(chainReference);
			return chainReference;
		} catch (FilterReferenceManager.InvalidFilterReferenceListException ifre) {
			throw RestUtils.createJsonFormattedException(ifre.getMessage(), 400);
		}
	}

}
