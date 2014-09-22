package org.jvalue.ods.qa.improvement;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class CombineSourceFilterTest {

	@Test
	public void testFilterEmptyObject() {
		List<Object> list = new LinkedList<Object>();
		new CombineSourceFilter(null, null).filter(list);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterNull() {
		new CombineSourceFilter(null, null).filter(null);

	}

}
