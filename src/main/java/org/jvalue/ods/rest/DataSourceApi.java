package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataSourceRepository;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/datasources")
@Produces(MediaType.APPLICATION_JSON)
public class DataSourceApi {

	private final DataSourceRepository sourceRepository;

	@Inject
	public DataSourceApi(DataSourceRepository sourceRepository) {
		this.sourceRepository = sourceRepository;
	}


	@GET
	public List<DataSource> getAllSources() {
		try {
			List<DataSource> sources = sourceRepository.getAll();
			for (DataSource source : sources) System.out.println("found source " + source.getSourceId());
			return sources;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@GET
	@Path("/{sourceId}")
	public DataSource getSingleSource(@PathParam("sourceId") String sourceId) {
		return assertIsValidSource(sourceId);
	}


	private DataSource assertIsValidSource(String sourceId) {
		List<DataSource> sources = sourceRepository.findBySourceId(sourceId);
		if (sources.isEmpty()) throw RestUtils.createNotFoundException();
		if (sources.size() > 1) throw new IllegalStateException("found more than one source of id " + sourceId);
		return sources.get(0);
	}

}
