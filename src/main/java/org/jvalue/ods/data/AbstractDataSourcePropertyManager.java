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

import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.jvalue.ods.db.DataRepository;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.RepositoryCache;
import org.jvalue.ods.utils.Assert;

import java.util.List;


public abstract class AbstractDataSourcePropertyManager<T, R extends CouchDbRepositorySupport<T>> {

	private final RepositoryCache<R> repositoryCache;
	private final DbFactory dbFactory;


	protected AbstractDataSourcePropertyManager(
			RepositoryCache<R> repositoryCache,
			DbFactory dbFactory) {

		Assert.assertNotNull(repositoryCache, dbFactory);
		this.repositoryCache = repositoryCache;
		this.dbFactory = dbFactory;
	}


	public final void add(DataSource source, DataRepository dataRepository, T data) {
		Assert.assertNotNull(source, dataRepository, data);
		assertRepository(source).add(data);
		doAdd(source, dataRepository, data);
	}


	protected abstract void doAdd(DataSource source, DataRepository dataRepository, T data);


	public final void remove(DataSource source, DataRepository dataRepository, T data) {
		Assert.assertNotNull(source, data);
		assertRepository(source).remove(data);
		doRemove(source, dataRepository, data);
	}


	protected abstract void doRemove(DataSource source, DataRepository dataRepository, T data);


	public final void removeAll(DataSource source) {
		R repository = assertRepository(source);
		for (T item : repository.getAll()) {
			repository.remove(item);
		}
		repositoryCache.remove(source.getSourceId());
	}


	public final T get(DataSource source, String propertyId) {
		Assert.assertNotNull(source, propertyId);
		return doGet(assertRepository(source), propertyId);
	}


	protected abstract T doGet(R repository, String propertyId);


	public final List<T> getAll(DataSource source) {
		Assert.assertNotNull(source);
		return assertRepository(source).getAll();
	}


	public final boolean contains(DataSource source, String propertyId) {
		try {
			get(source, propertyId);
			return true;
		} catch (DocumentNotFoundException dnfe) {
			return false;
		}
	}


	private R assertRepository(DataSource source) {
		String key = source.getSourceId();
		if (repositoryCache.contains(key)) return repositoryCache.get(key);
		R repository = createNewRepository(key, dbFactory);
		repositoryCache.put(key, repository);
		return repository;
	}


	protected abstract R createNewRepository(String sourceId, DbFactory dbFactory);

}
