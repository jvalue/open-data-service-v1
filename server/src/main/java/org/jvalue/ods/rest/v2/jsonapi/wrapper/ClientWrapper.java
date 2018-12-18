package org.jvalue.ods.rest.v2.jsonapi.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jvalue.ods.api.notifications.*;

import java.util.Collection;
import java.util.stream.Collectors;

@Schema(name = "clientData")
public class ClientWrapper implements JsonApiIdentifiable{

	private final Client client;

	private ClientWrapper(Client client) {
		this.client = client;
	}

	@Schema(name = "attributes", oneOf = {HttpClient.class, GcmClient.class, AmqpClient.class, NdsClient.class}, required = true)
	@JsonUnwrapped
	@JsonIgnoreProperties({"id", "type"})
	public Client getClient() {
		return client;
	}

	@Override
	public String getId() {
		return client.getId();
	}

	@Schema(allowableValues = {HttpClient.CLIENT_TYPE, GcmClient.CLIENT_TYPE, AmqpClient.CLIENT_TYPE, NdsClient.CLIENT_TYPE})
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
