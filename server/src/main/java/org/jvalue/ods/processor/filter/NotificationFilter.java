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
package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.NotificationManager;


final class NotificationFilter extends AbstractFilter<ObjectNode, ObjectNode> {

	private final NotificationManager notificationManager;
	private final DataSource source;

	@Inject
	public NotificationFilter(
			@Assisted DataSource source,
			NotificationManager notificationManager,
			MetricRegistry registry) {

		super(source, registry);
		this.notificationManager = notificationManager;
		this.source = source;
	}


	@Override
	protected ObjectNode doFilter(ObjectNode item) {
		notificationManager.onNewDataItem(source, item);
		return item;
	}


	@Override
	protected void doOnComplete() {
		notificationManager.onNewDataComplete(source);
	}

}

