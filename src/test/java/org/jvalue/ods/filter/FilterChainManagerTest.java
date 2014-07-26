package org.jvalue.ods.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;


public final class FilterChainManagerTest {

	private int filterCount = 0;


	@Test
	public final void testAddRemoveFilter() {

		List<FilterChain<Void, Void>> chains = new LinkedList<>();
		chains.add(FilterChain.instance(new DummyFilter()));
		chains.add(FilterChain.instance(new DummyFilter()));
		chains.add(FilterChain.instance(new DummyFilter()));

		FilterChainManager manager = new FilterChainManager();

		for (FilterChain<Void,?> chain : chains) {
			assertFalse(manager.isRegistered(chain));
			manager.register(chain);
			assertTrue(manager.isRegistered(chain));
			assertTrue(manager.getRegistered().contains(chain));
		}

		manager.startFilterChains();
		assertEquals(3, filterCount);

		for (FilterChain<Void,?> chain : chains) {
			manager.unregister(chain);
			assertFalse(manager.isRegistered(chain));
			assertFalse(manager.getRegistered().contains(chain));
		}

	}


	private class DummyFilter implements Filter<Void, Void> {

		@Override
		public Void filter(Void param) {
			filterCount++;
			return null;
		}

	}

}
