package org.jvalue.ods.qa.improvement;

import org.junit.Test;
import org.jvalue.ods.filter.FilterException;

import java.util.LinkedList;
import java.util.List;

public class CombineSourceFilterTest {

	@Test
	public void testFilterEmptyObject() throws FilterException {
		List<Object> list = new LinkedList<Object>();
		new CombineSourceFilter(null, null).filter(list);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFilterNull() throws FilterException {
		new CombineSourceFilter(null, null).filter(null);
	}

}
