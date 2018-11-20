package org.jvalue.ods;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ods.api.sources.DataSource;

public class DataSourceApiTest extends AbstractApiTest {

	@Test
	public void testCrud() {
		//test add and get
		Assert.assertEquals(dataSource, getEntityFromDoc(dataSourceApi.getSource(sourceId), DataSource.class, 0));
	}
}
