/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.admin.monitoring;


import com.codahale.metrics.*;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@RunWith(JMockit.class)
public class PauseableTimerTest {

	@Mocked Timer anyTimer;
	@Mocked MetricRegistry registry;


	@Test
	public void testRegularTimer() throws Exception {
		PauseableTimer timer = PauseableTimer.createTimer(registry, "someName");
		Assert.assertNotNull(timer.getTimer());
		PauseableTimer.Context context = timer.createContext();
		context.resume();
		Thread.sleep(100);
		context.stop();

		new Verifications() {{
			long time;
			Timer timer = new Timer((Reservoir) any, (Clock) any);
			timer.update(time = withCapture(), TimeUnit.NANOSECONDS);
			Assert.assertTrue(time > 0);
		}};
	}


	@Test
	public void testPauseableTimer() throws Exception {
		PauseableTimer timer = PauseableTimer.createTimer(registry, "someName");
		PauseableTimer.Context context = timer.createContext();
		context.resume();
		context.pause();
		Thread.sleep(100);
		context.resume();
		context.stop();

		new Verifications() {{
			long time;
			Timer timer = new Timer((Reservoir) any, (Clock) any);
			timer.update(time = withCapture(), TimeUnit.NANOSECONDS);
			Assert.assertTrue(time > 0);
			Assert.assertTrue(time < 100 * 1000);
		}};
	}


	@Test
	public void testTimerCaching() {
		final String name = "someName";
		final Map<String, Metric> timers = new TreeMap<>();

		new Expectations() {{
			registry.getTimers((MetricFilter) any);
			result = timers;
		}};

		PauseableTimer timer = PauseableTimer.createTimer(registry, name);
		timers.put(name, timer.getTimer());
		timer = PauseableTimer.createTimer(registry, name);

		new Verifications() {{
			registry.register(name, (Metric) any);
			times = 1;
		}};

	}
}
