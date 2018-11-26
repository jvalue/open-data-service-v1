package org.jvalue.ods.transformation;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;

public class ExecutionEngineModule  extends AbstractModule{
	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
			.implement(
				ExecutionEngine.class,
				Names.named(ExecutionEngineFactory.EXECUTION_ENGINE_IMPLEMENTATION),
				NashornExecutionEngine.class)

			.build(ExecutionEngineFactory.class));
	}
}
