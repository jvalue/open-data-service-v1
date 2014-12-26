package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;

import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import static org.junit.Assert.assertEquals;


@RunWith(JMockit.class)
public final class AbstractFilterTest {

	@Mocked
	private DataSource source;

	@Mocked
	private MetricRegistry registry;

	private int
			filterCount = 0,
			completeCount = 0;


	@Test
	public final void testChain() throws FilterException {

		String value = "dummy";

		AbstractFilter<String, String> chain = new DummyFilter(value);
		chain
			.setNextFilter(new DummyFilter(value))
			.setNextFilter(new DummyFilter(value))
			.setNextFilter(new DummyFilter(value));

		chain.filter(value);
		chain.onComplete();

		assertEquals(4, filterCount);
		assertEquals(4, completeCount);
	}


	private class DummyFilter extends AbstractFilter<String, String> {

		private String value;

		public DummyFilter(String value) {
			super(AbstractFilterTest.this.source, registry);
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
