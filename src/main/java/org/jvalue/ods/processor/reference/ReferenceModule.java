package org.jvalue.ods.processor.reference;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class ReferenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ProcessorReferenceFactory.class).in(Singleton.class);
	}

}
