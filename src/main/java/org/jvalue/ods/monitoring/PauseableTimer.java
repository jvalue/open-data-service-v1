package org.jvalue.ods.monitoring;


import com.codahale.metrics.Clock;
import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class PauseableTimer {

	public static PauseableTimer createTimer(MetricRegistry registry, String name) {
		Map<String, Timer> timers = registry.getTimers(new StringMetricFilter(name));
		if (timers.isEmpty()) {
			PauseableTimer timer = new PauseableTimer();
			registry.register(name, timer.getTimer());
			return timer;
		} else if (timers.size() == 1) return new PauseableTimer(timers.get(name));
		else throw new IllegalStateException("found more than one timer for name " + name);
	}


	private final Timer timer;
	private final Clock clock;


	private PauseableTimer(Timer timer) {
		this.timer = timer;
		this.clock = Clock.defaultClock();
	}


	private PauseableTimer() {
		this(new Timer(new ExponentiallyDecayingReservoir(), Clock.defaultClock()));
	}


	public Context createContext() {
		return new Context(timer, clock);
	}


	public Timer getTimer() {
		return timer;
	}


	public static class Context {

		private final Timer timer;
		private final Clock clock;

		private long startTime;
		private boolean started = false;

		private long pausedTime = 0;
		private long idleTime = 0;
		private boolean paused = false;

		private Context(Timer timer, Clock clock) {
			this.timer = timer;
			this.clock = clock;
		}


		public void pause() {
			if (paused) throw new IllegalStateException("already paused");
			paused = true;
			pausedTime = clock.getTick();
		}


		public void resume() {
			if (!started) {
				startTime = clock.getTick();
				started = true;
			} else {
				if (!paused) throw new IllegalStateException("not paused");
				paused = false;
				idleTime += (clock.getTick() - pausedTime);
			}
		}


		public long stop() {
			if (!started) throw new IllegalStateException("not started");
			if (paused) resume();
			long elapsedTime = clock.getTick() - startTime - idleTime;
			timer.update(elapsedTime, TimeUnit.NANOSECONDS);
			return elapsedTime;
		}

	}
}
