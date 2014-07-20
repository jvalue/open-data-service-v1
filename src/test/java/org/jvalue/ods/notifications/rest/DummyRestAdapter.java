package org.jvalue.ods.notifications.rest;

import java.util.Set;
import java.util.UUID;

import org.jvalue.ods.notifications.clients.DummyClient;
import org.restlet.Request;


final class DummyRestAdapter extends RestAdapter<DummyClient> {


	@Override
	protected DummyClient toClient(Request request, String source) {
		return new DummyClient(UUID.randomUUID().toString(), source);
	}


	@Override
	protected void getParameters(Set<String> params) {
	}

}
