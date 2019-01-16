/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.admin.monitoring.PauseableTimer;
import org.jvalue.ods.api.sources.DataSource;

public abstract class AbstractFilter<P, R> implements Filter<P, R> {

	protected final DataSource source;
	protected Filter<R, ?> nextFilter;

	private final PauseableTimer.Context timerContext;


	public AbstractFilter(DataSource source, MetricRegistry registry) {
		Assert.assertNotNull(source, registry);
		this.source = source;
		this.timerContext = PauseableTimer.createTimer(registry, MetricRegistry.name(this.getClass(), source.getId())).createContext();
	}


	@Override
	public final <T> Filter<R, T> setNextFilter(Filter<R, T> nextFilter) {
		Assert.assertNotNull(nextFilter);
		this.nextFilter = nextFilter;
		return nextFilter;
	}


	@Override
	public final void filter(P data) throws FilterException {
		timerContext.resume();
		R result = doFilter(data);
		timerContext.pause();
		if (nextFilter != null && result != null) nextFilter.filter(result);
	}


	@Override
	public final void onComplete() throws FilterException {
		timerContext.resume();
		doOnComplete();
		timerContext.stop();
		if (nextFilter != null) nextFilter.onComplete();
	}


	protected abstract R doFilter(P data) throws FilterException;
	protected abstract void doOnComplete() throws FilterException;

}
