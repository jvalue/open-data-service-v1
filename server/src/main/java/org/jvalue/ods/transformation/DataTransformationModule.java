package org.jvalue.ods.transformation;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class DataTransformationModule extends AbstractModule
{
	@Override
	protected void configure() {
		bind(ExecutionEngine.class).in(Singleton.class);
		bind(DataTransformationManager.class).in(Singleton.class);
	}
}
