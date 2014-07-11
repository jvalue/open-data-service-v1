package org.jvalue.ods.notifications.clients; 

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;



public final class DummyClient extends Client {


	@JsonCreator
	public DummyClient(
			@JsonProperty("id") String id, 
			@JsonProperty("source") String source) {

		super(id, source);
	}


	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		throw new UnsupportedOperationException("dummy");
	}


}
