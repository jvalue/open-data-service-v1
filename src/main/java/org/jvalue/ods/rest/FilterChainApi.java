package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.db.FilterChainReferenceRepository;
import org.jvalue.ods.filter.reference.FilterChainReference;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL + "/{sourceId}/filterChains")
@Produces(MediaType.APPLICATION_JSON)
public final class FilterChainApi extends AbstractApi {

	private final DataSourceRepository sourceRepository;
	private final FilterChainReferenceRepository referenceRepository;

	@Inject
	public FilterChainApi(
			DataSourceRepository sourceRepository,
			FilterChainReferenceRepository referenceRepository) {

		this.sourceRepository = sourceRepository;
		this.referenceRepository = referenceRepository;
	}


	@GET
	public List<FilterChainReference> getAllFilterChains(@PathParam("sourceId") String sourceId) {
		assertIsValidSource(sourceId);
		return referenceRepository.findByDataSourceId(sourceId);
	}


	@GET
	@Path("/{chainId}")
	public FilterChainReference getSingleFilterChain(
			@PathParam("sourceId") String sourceId,
			@PathParam("chainId") String chainId) {

		assertIsValidSource(sourceId);
		return referenceRepository.get(chainId);
	}


	private void assertIsValidSource(String sourceId) {
		List<DataSource> sources = sourceRepository.findBySourceId(sourceId);
		if (sources.isEmpty()) throw RestUtils.createNotFoundException();
		if (sources.size() > 1) throw new IllegalStateException("found more than one source of id " + sourceId);
	}

}
