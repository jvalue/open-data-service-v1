package org.jvalue.ods.notifications.clients; 



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
