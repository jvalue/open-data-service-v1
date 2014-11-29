package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DataSourceRepository;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(AbstractApi.BASE_URL)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class DataSourceApi extends AbstractApi {

	private final DataSourceRepository sourceRepository;

	@Inject
	public DataSourceApi(DataSourceRepository sourceRepository) {
		this.sourceRepository = sourceRepository;
	}


	@GET
	public List<DataSource> getAllSources() {
		return sourceRepository.getAll();
	}


	@GET
	@Path("/{sourceId}")
	public DataSource getSingleSource(@PathParam("sourceId") String sourceId) {
		return sourceRepository.findBySourceId(sourceId);
	}


	@POST
	public void addSource(DataSource dataSource) {
		sourceRepository.add(dataSource);
	}


	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(@PathParam("sourceId") String sourceId) {
		sourceRepository.remove(sourceRepository.findBySourceId(sourceId));
	}

}
