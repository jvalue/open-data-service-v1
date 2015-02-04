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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.AbstractDataSourcePropertyManager;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.db.NotificationClientRepository;
import org.jvalue.ods.notifications.sender.Sender;
import org.jvalue.ods.notifications.sender.SenderCache;
import org.jvalue.ods.notifications.sender.SenderResult;
import org.jvalue.ods.utils.Cache;
import org.jvalue.ods.utils.Log;


public final class NotificationManager
		extends AbstractDataSourcePropertyManager<Client, NotificationClientRepository>
		implements DataSink {


	private final SenderCache senderCache;

	@Inject
	NotificationManager(
			Cache<NotificationClientRepository> repositoryCache,
			RepositoryFactory repositoryFactory,
			SenderCache senderCache) {

		super(repositoryCache, repositoryFactory);
		this.senderCache = senderCache;
	}


	@Override
	public void onNewDataStart(DataSource source) {
		for (Client client : getAll(source)) {
			senderCache.get(source, client).onNewDataStart();
		}
	}


	@Override
	public void onNewDataItem(DataSource source, ObjectNode data) {
		for (Client client : getAll(source)) {
			senderCache.get(source, client).onNewDataItem(data);
		}
	}


	@Override
	public void onNewDataComplete(DataSource source) {
		for (Client client : getAll(source)) {
			Sender<?> sender = senderCache.get(source, client);
			senderCache.release(source, client);

			sender.onNewDataComplete();
			SenderResult result = sender.getSenderResult();
			switch(result.getStatus()) {
				case SUCCESS:
					break;

				case ERROR:
					String errorMsg = "Failed to send notification to client " + client.getId();
					if (result.getErrorCause() != null)
						errorMsg = errorMsg + " (" + result.getErrorCause().getMessage() + ")";
					if (result.getErrorMsg() != null)
						errorMsg = errorMsg + " (" + result.getErrorMsg() + ")";
					Log.error(errorMsg);
					break;

				case REMOVE_CLIENT:
					Log.info("Unregistering client " + result.getOldClient().getId());
					remove(source, null, result.getOldClient());
					break;

				case UPDATE_CLIENT:
					Log.info("Updating client id to " + result.getNewClient().getId());
					remove(source, null, result.getOldClient());
					add(source, null, result.getNewClient());
					break;
			}
		}
	}


	@Override
	protected void doAdd(DataSource source, DataRepository dataRepository, Client client) { }


	@Override
	protected void doRemove(DataSource source, DataRepository dataRepository, Client client) { }


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected NotificationClientRepository createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createNotificationClientRepository(sourceId);
	}

}
