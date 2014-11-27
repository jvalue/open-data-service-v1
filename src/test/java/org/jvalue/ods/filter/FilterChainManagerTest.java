package org.jvalue.ods.filter;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public final class FilterChainManagerTest {

	private int filterCount = 0;


	@Test
	public final void testAddRemoveFilter() {

		List<Filter<Void, Void>> chains = new LinkedList<>();
		chains.add(new DummyFilter());
		chains.add(new DummyFilter());
		chains.add(new DummyFilter());

		FilterChainManager manager = new FilterChainManager();

		for (Filter<Void,?> chain : chains) {
			assertFalse(manager.isRegistered(chain));
			manager.register(chain);
			assertTrue(manager.isRegistered(chain));
			assertTrue(manager.getRegistered().contains(chain));
		}

		manager.startFilterChains();
		assertEquals(3, filterCount);

		for (Filter<Void,?> chain : chains) {
			manager.unregister(chain);
			assertFalse(manager.isRegistered(chain));
			assertFalse(manager.getRegistered().contains(chain));
		}

	}


	private class DummyFilter extends Filter<Void, Void> {

		@Override
		protected Void doFilter(Void param) {
			filterCount++;
			return null;
		}

	}

}
