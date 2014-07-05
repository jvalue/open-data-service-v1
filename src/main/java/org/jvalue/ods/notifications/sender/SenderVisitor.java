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
package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.ApiKey;
import org.jvalue.ods.notifications.ClientVisitor;
import org.jvalue.ods.notifications.NotificationException;
import org.jvalue.ods.notifications.clients.GcmClient;
import org.jvalue.ods.utils.Assert;


public final class SenderVisitor implements ClientVisitor<DataSource, Void> {

	private final ApiKey key;

	public SenderVisitor(ApiKey key) throws NotificationException {
		Assert.assertNotNull(key);
		this.key = key;
	}

	public Void visit(GcmClient client, DataSource source) {
		SenderFactory.getGcmSender(key).notifySourceChanged(source, client);
		return null;
	}

}
