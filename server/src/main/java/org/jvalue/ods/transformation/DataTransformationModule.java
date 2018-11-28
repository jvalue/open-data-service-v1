package org.jvalue.ods.transformation;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.jvalue.ods.data.DataTransformationManager;

public final class DataTransformationModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(DataTransformationManager.class).in(Singleton.class);
	}
}
