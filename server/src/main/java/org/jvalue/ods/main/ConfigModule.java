/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.main;


import com.google.inject.AbstractModule;

public final class ConfigModule extends AbstractModule {

	private final OdsConfig config;

	public  ConfigModule(OdsConfig config) {
		this.config = config;
	}


	@Override
	protected void configure() {
		bind(String.class).annotatedWith(GcmApiKey.class).toInstance(config.getGcmApiKey());
	}

}
