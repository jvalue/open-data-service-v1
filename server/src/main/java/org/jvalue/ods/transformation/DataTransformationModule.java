/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.transformation;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class DataTransformationModule extends AbstractModule {
	@Override
	protected void configure() {
		ExecutionEngine executionEngine = new NashornExecutionEngine();
		bind(ExecutionEngine.class).toInstance(executionEngine);
		bind(DataTransformationManager.class).in(Singleton.class);
	}
}
