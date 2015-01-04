package org.jvalue.ods.sources;


import org.jvalue.ods.api.data.DataApi;
import org.jvalue.ods.api.processors.ProcessorApi;
import org.jvalue.ods.api.sources.*;
import org.jvalue.ods.api.views.DataViewApi;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public final class ApiFactory {

	private static final String SERVER_URL = "http://localhost:8080/ods/api/v1";

	private final RestAdapter restAdapter;


	public ApiFactory() {
		restAdapter = new RestAdapter.Builder()
				.setEndpoint(SERVER_URL)
				.setConverter(new JacksonConverter())
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						request.addHeader("Accept", "application/json");
						request.addHeader("Content-Type", "application/json");
					}
				})
				.build();
	}


	public DataSourceApi createDataSourceApi() {
		return restAdapter.create(DataSourceApi.class);
	}


	public ProcessorApi createProcessorApi() {
		return restAdapter.create(ProcessorApi.class);
	}


	public DataViewApi createDataViewApi() {
		return restAdapter.create(DataViewApi.class);
	}


	public DataApi createDataApi() {
		return restAdapter.create(DataApi.class);
	}

}
