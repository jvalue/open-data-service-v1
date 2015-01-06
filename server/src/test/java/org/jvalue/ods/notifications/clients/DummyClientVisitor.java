package org.jvalue.ods.notifications.clients;


import org.jvalue.ods.api.notifications.ClientVisitor;
import org.jvalue.ods.api.notifications.GcmClient;
import org.jvalue.ods.api.notifications.HttpClient;

public final class DummyClientVisitor implements ClientVisitor<String, String> {

	@Override
	public String visit(GcmClient client, String param) {
		return param;
	}


	@Override
	public String visit(HttpClient client, String param) {
		return param;
	}

}
