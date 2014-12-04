package org.jvalue.ods.filter.reference;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class ReferenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FilterReferenceFactory.class).in(Singleton.class);
	}

}
