package org.jvalue.ods.decoupleDatabase.couchdb.wrapper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.ods.api.processors.PluginMetaData;
import org.jvalue.ods.db.PluginMetaDataRepository;
import org.jvalue.ods.decoupleDatabase.IRepository;

import java.io.InputStream;
import java.util.List;

public class CouchDbPluginMetaDataRepositoryWrapper implements IRepository<PluginMetaData>{

	private PluginMetaDataRepository pluginMetaDataRepository;

	@Inject
	CouchDbPluginMetaDataRepositoryWrapper(DbConnectorFactory dbConnectorFactory, @Assisted String databaseName) {
		this.pluginMetaDataRepository = new PluginMetaDataRepository(dbConnectorFactory, databaseName);
	}


	@Override
	public PluginMetaData findById(String Id) {
		return pluginMetaDataRepository.findById(Id);
	}


	@Override
	public void add(PluginMetaData Value) {
		pluginMetaDataRepository.add(Value);
	}


	@Override
	public void update(PluginMetaData value) {
		pluginMetaDataRepository.update(value);
	}


	@Override
	public void remove(PluginMetaData Value) {
		pluginMetaDataRepository.remove(Value);
	}


	@Override
	public List<PluginMetaData> getAll() {
		return pluginMetaDataRepository.getAll();
	}

	public void addAttachment(PluginMetaData metaData, InputStream stream, String contentType) {
		pluginMetaDataRepository.addAttachment(metaData, stream, contentType);
	}

	public InputStream getAttachment(PluginMetaData metaData) {
		return pluginMetaDataRepository.getAttachment(metaData);
	}
}
