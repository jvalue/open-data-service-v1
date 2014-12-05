package org.jvalue.ods.rest.client;


import com.fasterxml.jackson.databind.node.ArrayNode;

import retrofit.http.GET;
import retrofit.http.Path;

public interface DataClient {

	static String DATA_URL = BaseClient.BASE_URL + "/datasources/{sourceId}/data";

	@GET(DATA_URL)
	public ArrayNode getAll(@Path("sourceId") String sourceId);

}
