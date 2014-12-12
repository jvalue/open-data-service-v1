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
package org.jvalue.ods.filter.adapter;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.filter.FilterException;
import org.jvalue.ods.utils.Assert;

import java.util.Iterator;


abstract class SourceAdapter extends Filter<Void, ObjectNode> implements Iterable<ObjectNode> {

	private final DataSource dataSource;

	protected SourceAdapter(DataSource dataSource) {
		Assert.assertNotNull(dataSource);
		this.dataSource = dataSource;
	}


	@Override
	public void filter(Void param) throws FilterException {
		try {
			for (ObjectNode node : this) {
				nextFilter.filter(node);
			}
		} catch (SourceAdapterException sae) {
			throw new FilterException(sae);
		}
	}


	@Override
	protected final ObjectNode doFilter(Void param) throws FilterException {
		// nothing to do here
		return null;
	}


	public final Iterator<ObjectNode> iterator() {
		return doCreateIterator(dataSource);
	}


	protected abstract SourceIterator doCreateIterator(DataSource source);


	static final class SourceAdapterException extends RuntimeException {

		public SourceAdapterException(Throwable cause) {
			super(cause);
		}

	}
}
