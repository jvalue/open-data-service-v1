package org.jvalue.ods.data;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class DataModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DataSourceManager.class).in(Singleton.class);
	}

}
