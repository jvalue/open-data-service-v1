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

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataViewRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.RepositoryCache2;
import org.jvalue.ods.utils.Assert;

import java.util.List;


public final class DataViewManager extends AbstractDataSourcePropertyManager<DataView, DataViewRepository> {


	@Inject
	DataViewManager(
			RepositoryCache2<DataViewRepository> viewRepositoryCache,
			DbFactory dbFactory) {

		super(viewRepositoryCache, dbFactory);
	}


	public List<JsonNode> executeView(DataRepository dataRepository, DataView view, String argument) {
		Assert.assertNotNull(dataRepository, view);
		return dataRepository.executeQuery(view, argument);
	}


	@Override
	protected void doAdd(DataSource source, DataRepository dataRepository, DataView dataView) {
		dataRepository.addView(dataView);
	}


	@Override
	protected void doRemove(DataSource source, DataRepository dataRepository, DataView dataView) {
		dataRepository.removeView(dataView);
	}


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected DataViewRepository createNewRepository(String sourceId, DbFactory dbFactory) {
		return dbFactory.createDataViewRepository(sourceId);
	}

}
