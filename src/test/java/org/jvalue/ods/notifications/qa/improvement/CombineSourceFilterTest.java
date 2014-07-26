package org.jvalue.ods.notifications.qa.improvement;

import org.junit.Test;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.qa.improvement.CombineSourceFilter;

public class CombineSourceFilterTest {

	@Test
	public void testFilterEmptyObject() {
		ListObject lo = new ListObject();

		GenericEntity ge_ret = new CombineSourceFilter().filter(lo);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterNull() {
		new CombineSourceFilter().filter(null);

	}

}
