package org.jvalue.ods.server.restlet; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.server.utils.RestletResult;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;


public final class BaseRestletTest {

	private int 
		doGetCounter = 0,
		doPostCounter = 0;

	@Before
	public void resetCounters() {
		doGetCounter = 0;
		doPostCounter = 0;
	}


	@Test
	public final void testNoParam() {
		Restlet restlet = new BaseRestlet() {
			@Override
			protected RestletResult doGet(Request request) {
				doGetCounter++;
				return RestletResult.newSuccessResult();
			}

			@Override
			protected RestletResult doPost(Request request) {
				doPostCounter++;
				return RestletResult.newSuccessResult();
			}
		};

		Request getRequest = new Request(Method.GET, new Reference());
		Response getResponse = new Response(getRequest);
		Request postRequest = new Request(Method.POST, new Reference());
		Response postResponse = new Response(postRequest);

		assertEquals(0, doGetCounter);
		restlet.handle(getRequest, getResponse);
		assertEquals(1, doGetCounter);
		assertTrue(getResponse.getStatus().isSuccess());

		assertEquals(0, doPostCounter);
		restlet.handle(postRequest, postResponse);
		assertEquals(1, doPostCounter);
		assertTrue(postResponse.getStatus().isSuccess());
	}


	@Test
	public final void testNoParamInvalidMethod() {
		Restlet restlet = new BaseRestlet() { };

		Request getRequest = new Request(Method.GET, new Reference());
		Response getResponse = new Response(getRequest);
		Request postRequest = new Request(Method.POST, new Reference());
		Response postResponse = new Response(postRequest);

		restlet.handle(getRequest, getResponse);
		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, getResponse.getStatus());

		restlet.handle(postRequest, postResponse);
		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, postResponse.getStatus());
	}


	@Test
	public final void testGetParameter() {

		Restlet restlet = new BaseRestlet(new HashSet<String>(), true) {
			@Override
			public RestletResult doGet(Request request) {
				doGetCounter++;
				assertEquals("value", getParameter(request, "key"));
				return RestletResult.newSuccessResult();
			}
		};

		Request request = new Request(Method.GET, new Reference());
		request.getResourceRef().addQueryParameter("key", "value");
		Response response = new Response(request);

		restlet.handle(request, response);
		assertEquals(1, doGetCounter);
	}


	@Test
	public final void testInvalidParamCount() {

		Restlet restlet = new BaseRestlet(
				new HashSet<String>(Arrays.asList("key1")), 
				false) {

			@Override
			public RestletResult doGet(Request request) {
				doGetCounter++;
				return RestletResult.newSuccessResult();
			}
		};

		Request request = new Request(Method.GET, new Reference());
		Response response = new Response(request);

		restlet.handle(request, response);
		assertEquals(0, doGetCounter);
		assertTrue(response.getStatus().isError());

		request.getResourceRef().addQueryParameter("key1", "value");
		response = new Response(request);

		restlet.handle(request, response);
		assertEquals(1, doGetCounter);
		assertTrue(response.getStatus().isSuccess());

		request.getResourceRef().addQueryParameter("key2", "value");
		response = new Response(request);

		restlet.handle(request, response);
		assertEquals(1, doGetCounter);
		assertTrue(response.getStatus().isError());
	}

}
