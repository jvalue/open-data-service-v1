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

import org.ektorp.DocumentNotFoundException;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DataViewRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.RepositoryCache;
import org.jvalue.ods.utils.Assert;

import java.util.List;


public final class DataViewManager {

	private final RepositoryCache<DataViewRepository> viewRepositoryCache;
	private final DbFactory dbFactory;


	@Inject
	DataViewManager(
			RepositoryCache<DataViewRepository> viewRepositoryCache,
			DbFactory dbFactory) {

		this.viewRepositoryCache = viewRepositoryCache;
		this.dbFactory = dbFactory;
	}


	public void add(DataSource source, DataRepository dataRepository, DataView dataView) {
		Assert.assertNotNull(source, dataRepository, dataView);

		// actually insert and check for validity
		dataRepository.addView(dataView);

		// persist view object
		DataViewRepository viewRepository = assertViewRepository(source);
		viewRepository.add(dataView);
	}


	public void remove(DataSource source, DataRepository dataRepository, DataView dataView) {
		Assert.assertNotNull(source, dataView);
		assertViewRepository(source).remove(dataView);
		// TODO remove from data repository
	}


	public void removeAll(DataSource source) {
		viewRepositoryCache.remove(source.getSourceId());
	}


	public DataView get(DataSource source, String viewId) {
		Assert.assertNotNull(source, viewId);
		return assertViewRepository(source).findByViewId(viewId);
	}


	public List<DataView> getAll(DataSource source) {
		Assert.assertNotNull(source);
		return assertViewRepository(source).getAll();
	}


	public boolean contains(DataSource source, String viewId) {
		try {
			get(source, viewId);
			return true;
		} catch (DocumentNotFoundException dnfe) {
			return false;
		}
	}


	public List<JsonNode> executeView(DataRepository dataRepository, DataView view) {
		return executeView(dataRepository, view, null);
	}


	public List<JsonNode> executeView(DataRepository dataRepository, DataView view, String argument) {
		Assert.assertNotNull(dataRepository, view);
		if (argument == null) return dataRepository.executeQuery(view);
		else return dataRepository.executeQuery(view, argument);
	}


	private DataViewRepository assertViewRepository(DataSource source) {
		String key = source.getSourceId();
		if (viewRepositoryCache.contains(key)) return viewRepositoryCache.get(key);

		// create new one
		DataViewRepository viewRepository = dbFactory.createDataViewRepository(key);
		viewRepositoryCache.put(key, viewRepository);
		return viewRepository;
	}

}
