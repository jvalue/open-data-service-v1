/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods;


import org.jvalue.commons.utils.HttpServiceCheck;
import org.jvalue.ods.api.DataApi;
import org.jvalue.ods.api.DataSourceApi;
import org.jvalue.ods.api.DataViewApi;
import org.jvalue.ods.api.ProcessorChainApi;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public final class ApiFactory {

	private static final String SERVER_URL = "http://localhost:8080/ods/api";

	private final RestAdapter restAdapter;


	public ApiFactory() {
		HttpServiceCheck.check(SERVER_URL + "/v1/version");

		restAdapter = new RestAdapter.Builder()
				.setEndpoint(SERVER_URL)
				.setConverter(new JacksonConverter())
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						request.addHeader("Accept", "application/json");
						request.addHeader("Content-Type", "application/json");
						request.addHeader("Authorization", "Basic YWRtaW5AYWRtaW5sYW5kLmNvbTphZG1pbjEyMw=="); // admin@adminland.org : admin123
					}
				})
				.build();
	}


	public DataSourceApi createDataSourceApi() {
		return restAdapter.create(DataSourceApi.class);
	}


	public ProcessorChainApi createProcessorChainApi() {
		return restAdapter.create(ProcessorChainApi.class);
	}


	public DataViewApi createDataViewApi() {
		return restAdapter.create(DataViewApi.class);
	}


	public DataApi createDataApi() {
		return restAdapter.create(DataApi.class);
	}

}
