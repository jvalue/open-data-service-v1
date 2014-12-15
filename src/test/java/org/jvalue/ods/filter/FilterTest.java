package org.jvalue.ods.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public final class FilterTest {

	private int
			filterCount = 0,
			completeCount = 0;


	@Test
	public final void testChain() throws FilterException {

		String value = "dummy";

		Filter<String, String> chain = new DummyFilter(value);
		chain
			.setNextFilter(new DummyFilter(value))
			.setNextFilter(new DummyFilter(value))
			.setNextFilter(new DummyFilter(value));

		chain.process(value);
		chain.onComplete();

		assertEquals(4, filterCount);
		assertEquals(4, completeCount);
	}


	private class DummyFilter extends Filter<String, String> {

		private String value;

		public DummyFilter(String value) {
			this.value = value;
		}

		@Override
		protected String doProcess(String param) {
			assertEquals(value, param);
			filterCount++;
			return param;
		}


		@Override
		protected void doOnComplete() {
			completeCount++;
		}
	}

}
