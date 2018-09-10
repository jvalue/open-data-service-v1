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
package org.jvalue.ods.processor.plugin;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.ods.db.couchdb.repositories.PluginMetaDataRepository;
import org.jvalue.ods.db.generic.RepositoryFactory;
import org.jvalue.ods.data.AbstractDataSourcePropertyManager;

import java.io.InputStream;


public final class PluginMetaDataManager extends AbstractDataSourcePropertyManager<PluginMetaData, GenericRepository<PluginMetaData>> {


	@Inject
	PluginMetaDataManager(
			Cache<GenericRepository<PluginMetaData>> repositoryCache,
			RepositoryFactory repositoryFactory) {

		super(repositoryCache, repositoryFactory);
	}


	public void addFile(DataSource source, PluginMetaData metaData, InputStream inputStream, String contentType) {
		Assert.assertNotNull(source, metaData, inputStream, contentType);
		add(source, null, metaData);
		((PluginMetaDataRepository) getRepository(source)).addAttachment(metaData, inputStream, contentType);
	}


	public InputStream getFile(DataSource source, PluginMetaData metaData) {
		Assert.assertNotNull(source, metaData);
		return ((PluginMetaDataRepository) getRepository(source)).getAttachment(metaData);
	}


	@Override
	protected void doAdd(DataSource source, GenericDataRepository<JsonNode> dataRepository, PluginMetaData metaData) { }


	@Override
	protected void doRemove(DataSource source, GenericDataRepository<JsonNode> dataRepository, PluginMetaData metaData) { }


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected GenericRepository<PluginMetaData> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createPluginMetaDataRepository(sourceId);
	}

}
