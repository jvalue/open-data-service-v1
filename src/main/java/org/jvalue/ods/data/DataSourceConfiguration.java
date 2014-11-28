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
package org.jvalue.ods.data;

import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.filter.reference.FilterChainReference;
import org.jvalue.ods.utils.Assert;

public final class DataSourceConfiguration {

	private final DataSource dataSource;
	private final FilterChainReference filterChainReference;
	private final DataRepository dataRepository;

	public DataSourceConfiguration(
			DataSource dataSource,
			FilterChainReference filterChainReference,
			DataRepository dataRepository) {

		Assert.assertNotNull(dataSource, filterChainReference, dataRepository);
		this.dataSource = dataSource;
		this.filterChainReference = filterChainReference;
		this.dataRepository = dataRepository;
	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public FilterChainReference getFilterChainReference() {
		return filterChainReference;
	}


	public DataRepository getDataRepository() {
		return dataRepository;
	}

}
