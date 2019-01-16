/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.admin.monitoring;


import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.google.common.base.Objects;

public final class StringMetricFilter implements MetricFilter {

	private final String name;

	public StringMetricFilter(String name) {
		this.name = name;
	}


	@Override
	public boolean matches(String name, Metric metric) {
		return Objects.equal(this.name, name);
	}

}
