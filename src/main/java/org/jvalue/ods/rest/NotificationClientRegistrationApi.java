package org.jvalue.ods.rest;


import com.google.inject.Inject;

import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.clients.ClientFactory;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.clients.HttpClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

@Path("/notification/client/{clientType}")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationClientRegistrationApi {

	private static final Map<String, ClientRestAdapter> adapters = new HashMap<>();

	static {
		adapters.put("gcm", new ClientRestAdapter(GcmClient.class) {
			@Override
			public Client toClient(MultivaluedMap<String, String> values) {
				return ClientFactory.newGcmClient(
						values.getFirst("source"),
						values.getFirst("gcmClientId"));
			}
		});
		adapters.put("http", new ClientRestAdapter(HttpClient.class) {
			@Override
			public Client toClient(MultivaluedMap<String, String> values) {
				return ClientFactory.newHttpClient(
						values.getFirst("source"),
						values.getFirst("restUrl"),
						values.getFirst("sourceParam"),
						Boolean.valueOf(values.getFirst("sendData")));
			}
		});
	}



	protected final NotificationManager manager;

	@Inject
	NotificationClientRegistrationApi(NotificationManager manager) {
		this.manager = manager;
	}


	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Client registerClient(
			@PathParam("clientType") String clientType,
			MultivaluedMap<String, String> form) {

		assertIsValidClientType(clientType);
		try {
			Client client = adapters.get(clientType).toClient(form);
			manager.registerClient(client);
			return client;
		} catch (IllegalArgumentException iae) {
			throw RestUtils.createJsonFormattedException("failed to parse content (" + iae.getMessage() + ")", 400);
		}
	}


	@DELETE
	@Path("/{clientId}")
	public void unregisterClient(
			@PathParam("clientType") String clientType,
			@PathParam("clientId") String clientId) {

		assertIsValidClientType(clientType, clientId);
		manager.unregisterClient(clientId);
	}


	@GET
	@Path("/{clientId}")
	public Client getClient(
			@PathParam("clientType") String clientType,
			@PathParam("clientId") String clientId) {

		assertIsValidClientType(clientType, clientId);
		return manager.getClientById(clientId);
	}


	@GET
	public List<Client> getAllClients(@PathParam("clientType") String clientType) {
		assertIsValidClientType(clientType);
		Set<Client> clients = manager.getAllClients();
		Iterator<Client> iter = clients.iterator();
		while (iter.hasNext()) {
			if (!iter.next().getClass().equals(adapters.get(clientType).clientClass)) iter.remove();
		}
		return new LinkedList<>(clients);
	}



	private void assertIsValidClientType(String clientTypePath) {
		if (!adapters.containsKey(clientTypePath)) throw RestUtils.createJsonFormattedException("invalid client type", 404);
	}


	private void assertIsValidClientType(String clientTypePath, String clientId) {
		assertIsValidClientType(clientTypePath);
		if (!manager.isClientRegistered(clientId)) throw RestUtils.createJsonFormattedException("client not registered", 400);
		Client client = manager.getClientById(clientId);
		if (!client.getClass().equals(adapters.get(clientTypePath).clientClass)) throw RestUtils.createJsonFormattedException("client not registered", 400);
	}


	private abstract static class ClientRestAdapter {

		private final Class<? extends Client> clientClass;

		public ClientRestAdapter(Class<? extends Client> clientClass) {
			this.clientClass = clientClass;
		}

		public abstract Client toClient(MultivaluedMap<String, String> values);

	}

}
