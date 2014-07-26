package org.jvalue.ods.notifications.rest;

import org.restlet.Request;
import org.restlet.data.Method;
import org.restlet.data.Reference;


abstract class BaseAdapterTest {

	protected final String PARAM_SOURCE = "source";


	protected Request createMockRequest() {
		return new Request(Method.POST, new Reference());
	}


	protected void addParameter(Request request, String param, String value) {
		request.getResourceRef().addQueryParameter(param, value);
	}

}
