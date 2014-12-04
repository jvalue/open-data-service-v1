package org.jvalue.ods.rest.client;


import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public final class ClientFactory {

	private static final String SERVER_URL = "http://localhost:8080/";

	private final RestAdapter restAdapter;


	public ClientFactory() {
		restAdapter = new RestAdapter.Builder()
				.setEndpoint(SERVER_URL)
				.setConverter(new JacksonConverter())
				.build();
	}


	public DataSourceClient getDataSourceClient() {
		return restAdapter.create(DataSourceClient.class);
	}


	public FilterChainClient getFilterChainClient() {
		return restAdapter.create(FilterChainClient.class);
	}


	public DataViewClient getDataViewClient() {
		return restAdapter.create(DataViewClient.class);
	}

}
