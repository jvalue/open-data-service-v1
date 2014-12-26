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
    
 */
package org.jvalue.ods.processor.adapter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.utils.Assert;

import java.util.Iterator;


abstract class AbstractSourceAdapter implements SourceAdapter {

	private final DataSource dataSource;
	private final MetricRegistry registry;

	protected AbstractSourceAdapter(DataSource dataSource, MetricRegistry registry) {
		Assert.assertNotNull(dataSource, registry);
		this.dataSource = dataSource;
		this.registry = registry;
	}


	public final Iterator<ObjectNode> iterator() {
		return doCreateIterator(dataSource, registry);
	}


	protected abstract SourceIterator doCreateIterator(DataSource source, MetricRegistry registry);


}
