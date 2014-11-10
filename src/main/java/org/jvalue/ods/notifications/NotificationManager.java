/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */
package org.jvalue.ods.notifications;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.definitions.NotificationDefinition;
import org.jvalue.ods.notifications.sender.NotificationSender;
import org.jvalue.ods.notifications.sender.SenderResult;
import org.jvalue.ods.utils.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public final class NotificationManager {

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) {
			/*
			instance = new Builder(store)
					.definition(
							GcmClient.class,
							DefinitionFactory.getGcmDefinition())
					.definition(
							HttpClient.class,
							DefinitionFactory.getRestDefinition())
					.build();
					*/
			// TODO!
		}
		return instance;
	}


	private final ClientRepository clientRepository;
	private final Map<Class<?>, NotificationDefinition<?>> definitions;


	private NotificationManager(
			ClientRepository clientRepository,
			Map<Class<?>, NotificationDefinition<?>> definitions) {

		Assert.assertNotNull(clientRepository, definitions);
		this.clientRepository = clientRepository;
		this.definitions = definitions;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	public void notifySourceChanged(DataSource source, Object data) {
		Assert.assertNotNull(source);
		for (Client client : clientRepository.findBySource(source.getId())) {
			NotificationSender sender = definitions.get(client.getClass()).getNotificationSender();
			if (sender == null) {
				Logging.error(NotificationManager.class, "Failed to get NotificationSender for client " + client.getClientId());
				continue;
			}

			SenderResult result = sender.notifySourceChanged(client, source, data);
			switch(result.getStatus()) {
				case SUCCESS:
					continue;

				case ERROR:
					String errorMsg = "Failed to send notification to client " + client.getClientId();
					if (result.getErrorCause() != null) 
						errorMsg = errorMsg + " (" + result.getErrorCause().getMessage();
					if (result.getErrorMsg() != null)
						errorMsg = errorMsg + " (" + result.getErrorMsg();
					Logging.error(NotificationManager.class, errorMsg);
					break;

				case REMOVE_CLIENT:
					Logging.info(NotificationSender.class, "Unregistering client " + result.getOldClient().getClientId());
					unregisterClient(result.getOldClient().getClientId());
					break;
					
				case UPDATE_CLIENT:
					Logging.info(NotificationSender.class, "Updating client id to " + result.getNewClient().getClientId());
					unregisterClient(result.getOldClient().getClientId());
					registerClient(result.getNewClient());
					break;

			}
		}
	}


	public Collection<NotificationDefinition<?>> getNotificationDefinitions() {
		return definitions.values();
	}


	public void registerClient(Client client) {
		Assert.assertNotNull(client);
		clientRepository.add(client);
	}


	public void unregisterClient(String clientId) {
		Assert.assertNotNull(clientId);
		for (Client client : clientRepository.findByClientId(clientId)) {
			clientRepository.remove(client);
		}
	}


	public Set<Client> getAllClients() {
		return new HashSet<Client>(clientRepository.getAll());
	}



	public static class Builder {

		private final ClientRepository clientRepository;
		private final Map<Class<?>, NotificationDefinition<?>> definitions;

		public Builder(ClientRepository clientRepository) {
			Assert.assertNotNull(clientRepository);
			this.clientRepository = clientRepository;
			this.definitions = new HashMap<>();
		}


		public <T extends Client> Builder definition(
				Class<T> clientType,
				NotificationDefinition<T> definition) {
			definitions.put(clientType, definition);
			return this;
		}


		public NotificationManager build() {
			return new NotificationManager(clientRepository, definitions);
		}

	}

}
