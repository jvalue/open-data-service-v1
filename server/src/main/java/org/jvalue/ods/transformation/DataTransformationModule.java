package org.jvalue.ods.transformation;

import com.google.inject.AbstractModule;

public final class DataTransformationModule extends AbstractModule
{
	@Override
	protected void configure() {
		ExecutionEngine nashornExecutionEngine = new NashornExecutionEngine();
		bind(ExecutionEngine.class).toInstance(nashornExecutionEngine);
	}
}
