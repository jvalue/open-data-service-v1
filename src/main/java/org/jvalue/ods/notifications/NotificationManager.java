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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.notifications.db.ClientDatastoreFactory;
import org.jvalue.ods.notifications.definitions.DefinitionFactory;
import org.jvalue.ods.utils.Assert;


public final class NotificationManager {

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) instance = new NotificationManager();
		return instance;
	}


	private final ClientDatastore clientStore;
	private final Map<Class<?>, NotificationDefinition<?>> definitions;

	private NotificationManager() {
		this.clientStore = ClientDatastoreFactory.getCouchDbClientDatastore();
		this.definitions = new HashMap<Class<?>, NotificationDefinition<?>>();

		definitions.put(GcmClient.class, DefinitionFactory.getGcmDefinition());
	}
	

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void notifySourceChanged(DataSource source) {
		Assert.assertNotNull(source);
		for (Client client : clientStore.getAll()) {
			NotificationSender sender = definitions.get(client.getClass()).getNotificationSender();
			sender.notifySourceChanged(source, client);
		}
	}


	public void registerClient(Client client) {
		Assert.assertNotNull(client);
		clientStore.add(client);
	}


	public void unregisterClient(Client client) {
		Assert.assertNotNull(client);
		clientStore.remove(client);
	}


	public Set<Client> getAllClients() {
		return clientStore.getAll();
	}

}
