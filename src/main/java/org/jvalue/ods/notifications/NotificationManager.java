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

import com.google.inject.Inject;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.sender.Sender;
import org.jvalue.ods.notifications.sender.SenderResult;
import org.jvalue.ods.notifications.sender.SenderVisitor;
import org.jvalue.ods.utils.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public final class NotificationManager {

	private final ClientRepository clientRepository;
	private final SenderVisitor senderVisitor;


	@Inject
	NotificationManager(
			ClientRepository clientRepository,
			SenderVisitor senderVisitor) {

		this.clientRepository = clientRepository;
		this.senderVisitor = senderVisitor;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	public void notifySourceChanged(DataSource source, Object data) {
		Assert.assertNotNull(source);

		SenderVisitor.DataEntry dataEntry = new SenderVisitor.DataEntry(source, data);
		for (Client client : clientRepository.findBySource(source.getId())) {
			SenderResult result = client.accept(senderVisitor, dataEntry);
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
					Logging.info(Sender.class, "Unregistering client " + result.getOldClient().getClientId());
					unregisterClient(result.getOldClient().getClientId());
					break;
					
				case UPDATE_CLIENT:
					Logging.info(Sender.class, "Updating client id to " + result.getNewClient().getClientId());
					unregisterClient(result.getOldClient().getClientId());
					registerClient(result.getNewClient());
					break;

			}
		}
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


	public boolean isClientRegistered(String clientId) {
		return !clientRepository.findByClientId(clientId).isEmpty();
	}


	public Client getClientById(String clientId) throws ClientNotRegisteredException {
		if (!isClientRegistered(clientId)) throw new ClientNotRegisteredException(clientId);
		List<Client> clients = clientRepository.findByClientId(clientId);
		return clients.get(0);
	}

}
