/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.data;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public final class DataModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DataSourceManager.class).in(Singleton.class);
	}

}
