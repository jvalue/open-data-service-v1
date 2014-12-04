package org.jvalue.ods.rest.client;


import org.jvalue.ods.rest.model.DataSource;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface DataSourceClient {

	static String SOURCE_URL = BaseClient.BASE_URL + "/datasources";

	@GET(SOURCE_URL)
	public List<DataSource> getAll();


	@GET(SOURCE_URL + "/{id}")
	public DataSource get(@Path("id") String id);


	@DELETE(SOURCE_URL + "/{id}")
	public Response remove(@Path("id") String id);


	@PUT(SOURCE_URL + "/{id}")
	public DataSource add(@Path("id") String id, @Body DataSource source);

}
