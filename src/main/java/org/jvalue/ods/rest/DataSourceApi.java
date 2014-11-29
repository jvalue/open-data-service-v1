package org.jvalue.ods.rest;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.inject.Inject;

import org.ektorp.DocumentNotFoundException;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceMetaData;
import org.jvalue.ods.db.DataSourceRepository;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.JsonPointerDeserializer;
import org.jvalue.ods.utils.JsonPointerSerializer;

import java.net.URL;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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


	@PUT
	@Path("/{sourceId}")
	public DataSource addSource(@PathParam("sourceId") String sourceId, DataSourceDescription sourceDescription) {
		try {
			sourceRepository.findBySourceId(sourceId);
			throw RestUtils.createJsonFormattedException("source with id " + sourceId + " already exists", 409);
		} catch (DocumentNotFoundException dnfe) {
			// source does not exist --> continue;
		}

		DataSource source = new DataSource(
				sourceId,
				sourceDescription.url,
				sourceDescription.domainIdKey,
				sourceDescription.schema,
				sourceDescription.metaData);
		sourceRepository.add(source);
		return source;
	}


	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(@PathParam("sourceId") String sourceId) {
		sourceRepository.remove(sourceRepository.findBySourceId(sourceId));
	}


	private static final class DataSourceDescription {
		private final URL url;
		private final DataSourceMetaData metaData;
		@JsonSerialize(using = JsonPointerSerializer.class)
		@JsonDeserialize(using = JsonPointerDeserializer.class)
		private final JsonPointer domainIdKey;
		private final JsonNode schema;

		@JsonCreator
		public DataSourceDescription(
				@JsonProperty("url") URL url,
				@JsonProperty("domainIdKey") JsonPointer domainIdKey,
				@JsonProperty("schema") JsonNode schema,
				@JsonProperty("metaData") DataSourceMetaData metaData) {

			Assert.assertNotNull(url, domainIdKey, schema, metaData);
			this.url = url;
			this.domainIdKey = domainIdKey;
			this.schema = schema;
			this.metaData = metaData;
		}

	}

}
