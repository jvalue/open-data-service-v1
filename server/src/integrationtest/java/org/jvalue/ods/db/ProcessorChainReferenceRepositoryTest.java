/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.db;


import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.couchdb.test.AbstractRepositoryAdapterTest;
import org.jvalue.ods.api.processors.ExecutionInterval;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ProcessorChainReferenceRepositoryTest extends AbstractRepositoryAdapterTest<ProcessorReferenceChain> {

	@Override
	protected RepositoryAdapter<?, ?, ProcessorReferenceChain> doCreateAdapter(DbConnectorFactory dbConnectorFactory) {
		return new ProcessorChainReferenceRepository(dbConnectorFactory, getClass().getSimpleName());
	}


	@Override
	protected ProcessorReferenceChain doCreateValue(String id, String data) {
		return new ProcessorReferenceChain(
				id,
				Arrays.asList(new ProcessorReference.Builder(data).build()),
				new ExecutionInterval(0, TimeUnit.SECONDS));
	}

}
