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
package org.jvalue.ods.configuration;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.filter.FilterChain;

import com.fasterxml.jackson.databind.JsonNode;


interface Configuration {

	public DataSource getDataSource();
	public FilterChain<Void,?> getFilterChain(DbAccessor<JsonNode> accessor);

}
