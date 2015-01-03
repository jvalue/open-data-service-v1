package org.jvalue.ods.notifications;


public class ClientNotRegisteredException extends RuntimeException {

	public ClientNotRegisteredException(String clientId) {
		super("client with id \"" + clientId + "\" is not registered");
	}

}
