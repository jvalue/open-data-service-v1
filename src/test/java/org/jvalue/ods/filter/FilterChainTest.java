package org.jvalue.ods.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public final class FilterChainTest {

	private int filterCount = 0;


	@Test
	public final void testChain() {

		String value = "dummy";

		FilterChain<String, String> chain = FilterChain.instance(new DummyFilter(value));
		chain
			.setNextFilter(new DummyFilter(value))
			.setNextFilter(new DummyFilter(value))
			.setNextFilter(new DummyFilter(value));

		chain.filter(value);

		assertEquals(4, filterCount);

	}


	private class DummyFilter implements Filter<String, String> {

		private String value;

		public DummyFilter(String value) {
			this.value = value;
		}

		@Override
		public String filter(String param) {
			assertEquals(value, param);
			filterCount++;
			return param;
		}

	}

}
