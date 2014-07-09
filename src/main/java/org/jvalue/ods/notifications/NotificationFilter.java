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
package org.jvalue.ods.notifications;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.filter.Filter;


public final class NotificationFilter implements Filter<GenericEntity, GenericEntity> {

	@Override
	public GenericEntity filter(DataSource source, GenericEntity data) {
		NotificationManager.getInstance().notifySourceChanged(source, data);
		return data;
	}

}

