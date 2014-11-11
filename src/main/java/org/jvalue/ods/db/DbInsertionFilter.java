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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.utils.Log;
import org.jvalue.ods.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


public final class DbInsertionFilter implements Filter<Object, Object> {

	private final DbAccessor<JsonNode> accessor;
	private final DataSource source;

	public DbInsertionFilter(DbAccessor<JsonNode> accessor, DataSource source) {
		Assert.assertNotNull(accessor, source);
		this.accessor = accessor;
		this.source = source;
	}


	@Override
	@SuppressWarnings("unchecked")
	public Object filter(Object data) {
		if (data instanceof List) {
			List<Map<String, Object>> list = (List<Map<String, Object>>) data;
			accessor.executeBulk(list, source.getImprovedDbSchema());

		} else if (data instanceof Map) {
			List<Map<String, Object>> list = new LinkedList<>();
			list.add((Map<String, Object>) data);
			accessor.executeBulk(list, source.getImprovedDbSchema());

		} else {
			String errmsg = "data must be a list or map.";
			Log.error(errmsg);
			throw new RuntimeException(errmsg);
		}
		return data;
	}

}
