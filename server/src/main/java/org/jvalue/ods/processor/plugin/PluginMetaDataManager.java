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

import org.jvalue.commons.utils.Assert;
import org.jvalue.commons.utils.Cache;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.DataView;
import org.jvalue.ods.data.AbstractDataSourcePropertyManager;
import org.jvalue.ods.db.RepositoryFactory;
import org.jvalue.ods.decoupleDatabase.IDataRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;
import org.jvalue.ods.decoupleDatabase.couchdb.wrapper.CouchDbPluginMetaDataRepositoryWrapper;

import java.io.InputStream;


public final class PluginMetaDataManager extends AbstractDataSourcePropertyManager<PluginMetaData, IRepository<PluginMetaData>> {


	@Inject
	PluginMetaDataManager(
			Cache<IRepository<PluginMetaData>> repositoryCache,
			RepositoryFactory repositoryFactory) {

		super(repositoryCache, repositoryFactory);
	}


	public void addFile(DataSource source, PluginMetaData metaData, InputStream inputStream, String contentType) {
		Assert.assertNotNull(source, metaData, inputStream, contentType);
		add(source, null, metaData);
		((CouchDbPluginMetaDataRepositoryWrapper)getRepository(source)).addAttachment(metaData, inputStream, contentType);
	}


	public InputStream getFile(DataSource source, PluginMetaData metaData) {
		Assert.assertNotNull(source, metaData);
		return ((CouchDbPluginMetaDataRepositoryWrapper)getRepository(source)).getAttachment(metaData);
	}


	@Override
	protected void doAdd(DataSource source, IDataRepository<JsonNode> dataRepository, PluginMetaData metaData) { }


	@Override
	protected void doRemove(DataSource source, IDataRepository<JsonNode> dataRepository, PluginMetaData metaData) { }


	@Override
	protected void doRemoveAll(DataSource source) { }


	@Override
	protected IRepository<PluginMetaData> createNewRepository(String sourceId, RepositoryFactory repositoryFactory) {
		return repositoryFactory.createPluginMetaDataRepository(sourceId);
	}

}
