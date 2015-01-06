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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.data.AbstractDataSourcePropertyManager2;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.NotificationClientRepository;
import org.jvalue.ods.db.RepositoryCache2;
import org.jvalue.ods.notifications.clients.Client;
import org.jvalue.ods.notifications.sender.SenderResult;
import org.jvalue.ods.notifications.sender.SenderVisitor;
import org.jvalue.ods.utils.Assert;
import org.jvalue.ods.utils.Log;


public final class NotificationManager extends AbstractDataSourcePropertyManager2<Client, NotificationClientRepository> {

	private final SenderVisitor senderVisitor;


	@Inject
	NotificationManager(
			RepositoryCache2<NotificationClientRepository> repositoryCache,
			DbFactory dbFactory,
			SenderVisitor senderVisitor) {

		super(repositoryCache, dbFactory);
		this.senderVisitor = senderVisitor;
	}


	public void notifySourceChanged(DataSource source, ArrayNode data) {
		Assert.assertNotNull(source);

		SenderVisitor.DataEntry dataEntry = new SenderVisitor.DataEntry(source, data);
		for (Client client : getAll(source)) {
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
					Log.error(errorMsg);
					break;

				case REMOVE_CLIENT:
					Log.info("Unregistering client " + result.getOldClient().getClientId());
					remove(source, null, result.getOldClient());
					break;
					
				case UPDATE_CLIENT:
					Log.info("Updating client id to " + result.getNewClient().getClientId());
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
	protected NotificationClientRepository createNewRepository(String sourceId, DbFactory dbFactory) {
		return dbFactory.createNotificationClientRepository(sourceId);
	}

}
