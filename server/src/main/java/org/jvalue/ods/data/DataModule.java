package org.jvalue.ods.data;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.jvalue.commons.db.GenericRepository;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.db.couchdb.DataRepository;
import org.jvalue.ods.db.couchdb.DataSourceRepository;

public final class DataModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DataSourceManager.class).in(Singleton.class);
	}

}
