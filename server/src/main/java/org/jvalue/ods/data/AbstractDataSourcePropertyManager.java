package org.jvalue.ods.data;


import com.fasterxml.jackson.databind.JsonNode;
import org.ektorp.DocumentNotFoundException;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.commons.db.IRepository;

import java.util.List;


public abstract class AbstractDataSourcePropertyManager<T, R extends IRepository<T>> {

	private final Cache<R> repositoryCache;
	private final RepositoryFactory repositoryFactory;


	protected AbstractDataSourcePropertyManager(
			Cache<R> repositoryCache,
			RepositoryFactory repositoryFactory) {

		Assert.assertNotNull(repositoryCache, repositoryFactory);
		this.repositoryCache = repositoryCache;
		this.repositoryFactory = repositoryFactory;
	}


	public final void add(DataSource source, IDataRepository<CouchDbDataView, JsonNode> dataRepository, T data) {
		Assert.assertNotNull(source, data);
		assertRepository(source).add(data);
		doAdd(source, dataRepository, data);
	}


	protected abstract void doAdd(DataSource source, IDataRepository<CouchDbDataView, JsonNode> dataRepository, T data);


	public final void remove(DataSource source, IDataRepository<CouchDbDataView, JsonNode> dataRepository, T data) {
		Assert.assertNotNull(source, data);
		assertRepository(source).remove(data);
		doRemove(source, dataRepository, data);
	}


	protected abstract void doRemove(DataSource source, IDataRepository<CouchDbDataView, JsonNode> dataRepository, T data);


	public final void removeAll(DataSource source) {
		doRemoveAll(source);
		R repository = assertRepository(source);
		for (T item : repository.getAll()) {
			repository.remove(item);
		}
		repositoryCache.remove(source.getId());
	}


	protected abstract void doRemoveAll(DataSource source);


	public final T get(DataSource source, String propertyId) {
		Assert.assertNotNull(source, propertyId);
		return assertRepository(source).findById(propertyId);
	}


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


	protected R getRepository(DataSource source) {
		return assertRepository(source);
	}


	private R assertRepository(DataSource source) {
		String key = source.getId();
		if (repositoryCache.contains(key)) return repositoryCache.get(key);
		R repository = createNewRepository(key, repositoryFactory);
		repositoryCache.put(key, repository);
		return repository;
	}


	protected abstract R createNewRepository(String sourceId, RepositoryFactory repositoryFactory);

}
