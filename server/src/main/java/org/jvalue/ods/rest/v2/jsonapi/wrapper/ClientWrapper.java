package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import org.jvalue.ods.api.notifications.Client;

import java.util.Collection;
import java.util.stream.Collectors;

public class ClientWrapper implements JsonApiIdentifiable{

	private final Client client;

	private ClientWrapper(Client client) {
		this.client = client;
	}


	@Override
	public String getId() {
		return client.getId();
	}


	@Override
	public String getType() {
		return client.getType();
	}


	public static ClientWrapper from(Client client) {
		return new ClientWrapper(client);
	}


	public static Collection<ClientWrapper> fromCollection(Collection<Client> clients) {
		return clients.stream()
			.map(ClientWrapper::from)
			.collect(Collectors.toList());
	}
}
