package org.jvalue.ods.api.data;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.jvalue.ods.api.sources.DataSourceApi;

import retrofit.http.GET;
import retrofit.http.Path;

public interface DataApi {

	static String URL_DATA = DataSourceApi.URL_DATASOURCES + "/{sourceId}/data";

	@GET(URL_DATA)
	public ArrayNode getAll(@Path("sourceId") String sourceId);


	@GET(URL_DATA + "/{dataPath}")
	public JsonNode get(@Path("sourceId") String sourceId, @Path("dataPath") String dataPath);

}
