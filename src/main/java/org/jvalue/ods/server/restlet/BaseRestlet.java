package org.jvalue.ods.server.restlet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ods.server.utils.RestletResult;
import org.jvalue.ods.utils.Assert;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Status;


public abstract class BaseRestlet extends Restlet {

	private final Set<String> mandatoryQueryParams, optionalQueryParams;

	protected BaseRestlet(
			Set<String> mandatoryQueryParams,
			Set<String> optionalQueryParams) {

		Assert.assertNotNull(mandatoryQueryParams, optionalQueryParams);
		Assert.assertTrue(
				Collections.disjoint(mandatoryQueryParams, optionalQueryParams),
				"param sets must be disjoint");

		this.mandatoryQueryParams = mandatoryQueryParams;
		this.optionalQueryParams = optionalQueryParams;
	}


	protected BaseRestlet() {
		this(new HashSet<String>(), new HashSet<String>());
	}


	@Override
	public final void handle(Request request, Response response) {
		RestletResult result = handleRequest(request);
		response.setStatus(result.getStatus());
		if (result.getData() != null) {
			response.setEntity(result.getData().toString(), MediaType.APPLICATION_JSON);
		}
	}


	private final RestletResult handleRequest(Request request) {
		// validate params
		Set<String> paramNames = request.getResourceRef().getQueryAsForm().getNames();
		for (String param : mandatoryQueryParams) {
			if (!paramNames.remove(param)) {
				return onBadRequest("missing query param " + param); 
			}
		}
		paramNames.removeAll(optionalQueryParams);
		if (paramNames.size() > 0) {
			return onBadRequest("found unknown query params");
		}

		// validate method type
		Method method = request.getMethod();
		if (method.equals(Method.GET)) return doGet(request);
		else if (method.equals(Method.POST)) return doPost(request);
		else return onInvalidMethod(method);
	}


	protected RestletResult doGet(Request request) {
		return onInvalidMethod(Method.GET);
	}

	protected RestletResult doPost(Request request) {
		return onInvalidMethod(Method.POST);
	}


	protected final RestletResult onBadRequest(String msg) {
		return RestletResult.newErrorResult(Status.CLIENT_ERROR_BAD_REQUEST, msg);
	}


	protected final RestletResult onInvalidMethod(Method method) {
		return RestletResult.newErrorResult(
				Status.CLIENT_ERROR_BAD_REQUEST,
				"method " + method.toString() + " not supported");
	}


	protected final String getParameter(Request request, String key) {
		Parameter param = request.getResourceRef().getQueryAsForm().getFirst(key);
		if (param == null) return null;
		else return param.getValue();
	}

}
