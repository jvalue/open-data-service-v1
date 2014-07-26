package org.jvalue.ods.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public final class RestCallTest {

	@Test
	public void testSuccess() throws RestException {

		RestCall call = new RestCall.Builder(
				RestCall.RequestType.GET, 
				"http://pegelonline.wsv.de")
			.path("webservices")
			.path("rest-api")
			.path("v2")
			.path("stations.json")
			.parameter("waters", "RHEIN")
			.header("From", "me@home.com")
			.build();

		assertNotNull(call.execute());

	}


	@Test(expected = RestException.class)
	public void testFailure() throws RestException {

		RestCall call = new RestCall.Builder(
				RestCall.RequestType.POST, 
				"http://pegelonline.wsv.de")
			.path("dummy")
			.build();

		call.execute();

	}


}
