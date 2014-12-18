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

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;


public interface SourceAdapter extends Iterable<ObjectNode> {

	/**
	 * @return an iterator used to stream access the data of this source. Note that iterator will
	 * throw an {@link org.jvalue.ods.processor.adapter.SourceAdapterException} in case of
	 * any errors.
	 */
	public Iterator<ObjectNode> iterator();

}
