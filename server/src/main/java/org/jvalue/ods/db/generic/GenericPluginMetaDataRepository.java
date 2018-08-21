package org.jvalue.ods.db.generic;

import org.jvalue.commons.EntityBase;
import org.jvalue.commons.db.repositories.GenericRepository;
import org.jvalue.ods.api.processors.PluginMetaData;

import java.io.InputStream;

public interface GenericPluginMetaDataRepository<T extends EntityBase> extends GenericRepository<T> {
	void addAttachment(PluginMetaData metaData, InputStream stream, String contentType);

	InputStream getAttachment(PluginMetaData metaData);
}
