/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.db;


import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.commons.couchdb.test.AbstractRepositoryAdapterTest;
import org.jvalue.ods.api.views.DataView;

public class DataViewRepositoryTest extends AbstractRepositoryAdapterTest<DataView> {

	@Override
	protected RepositoryAdapter<?, ?, DataView> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new DataViewRepository(connectorFactory, getClass().getSimpleName());
	}


	@Override
	protected DataView doCreateValue(String id, String data) {
		return new DataView(id, data);
	}

}
