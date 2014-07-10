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
package org.jvalue.ods.db;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.DataGrabberMain;
import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


public final class DbInsertionFilter implements Filter<GenericEntity, GenericEntity> {

	private final DbAccessor<JsonNode> accessor;
	private final DataSource source;

	public DbInsertionFilter(DbAccessor<JsonNode> accessor, DataSource source) {
		Assert.assertNotNull(accessor, source);
		this.accessor = accessor;
		this.source = source;
	}


	@Override
	public GenericEntity filter(GenericEntity data) {
		if (data instanceof ListObject) {

			ListObject lv = (ListObject) data;
			List<MapObject> list = new LinkedList<>();

			for (Serializable i : lv.getList()) {
				list.add((MapObject) i);
			}

			accessor.executeBulk(list, source.getDbSchema());
		} else if (data instanceof MapObject) {
			LinkedList<MapObject> list = new LinkedList<>();
			list.add((MapObject) data);
			accessor.executeBulk(list, source.getDbSchema());
		} else {
			String errmsg = "GenericValue must be ListValue or MapValue.";
			System.err.println(errmsg);
			Logging.error(DataGrabberMain.class, errmsg);
			throw new RuntimeException(errmsg);
		}
		return data;
	}

}
