/*  Open Data Service
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
package org.jvalue.ods.filter;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.NotificationManager;
import org.jvalue.ods.utils.Assert;


final class NotificationFilter implements Filter<ArrayNode, ArrayNode> {

	private final NotificationManager notificationManager;
	private final DataSource source;

	@Inject
	public NotificationFilter(
			NotificationManager notificationManager,
			@Assisted DataSource source) {

		Assert.assertNotNull(source);
		this.notificationManager = notificationManager;
		this.source = source;
	}


	@Override
	public ArrayNode filter(ArrayNode data) {
		notificationManager.notifySourceChanged(source, data);
		return data;
	}

}

