package org.jvalue.ods.notifications.clients; 



final class DummyClient extends Client {


	public DummyClient(String id, String source) {
		super(id, source);
	}


	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		throw new UnsupportedOperationException("dummy");
	}


}
