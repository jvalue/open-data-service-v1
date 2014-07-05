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
import org.jvalue.ods.notifications.db.ClientDatastoreFactory;
import org.jvalue.ods.notifications.sender.SenderVisitor;


final class NotificationManager {

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) instance = new NotificationManager();
		return instance;
	}


	private final SenderVisitor sender;
	private final ClientDatastore clientStore;

	private NotificationManager() {
		this.clientStore = ClientDatastoreFactory.getCouchDbClientDatastore();

		SenderVisitor sender = null;
		try {
			sender = new SenderVisitor(ApiKey.getInstance());
		} catch (NotificationException ne) {
			ne.printStackTrace(System.err);
		}
		this.sender = sender;
	}
	

	public void notifySourceChanged(DataSource source) {
		for (Client client : clientStore.getAll()) {
			client.accept(sender, source);
		}
	}

}
