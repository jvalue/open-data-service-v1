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
package org.jvalue.ods.db;


import com.google.common.base.Objects;

import org.jvalue.ods.utils.Assert;

public final class DbView {

	private final String viewName, mapFunction;

	public DbView(String viewName, String mapFunction) {
		Assert.assertNotNull(viewName, mapFunction);
		this.viewName = viewName;
		this.mapFunction = mapFunction;
	}

	public String getViewName() {
		return viewName;
	}


	public String getMapFunction() {
		return mapFunction;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DbView)) return false;
		DbView view = (DbView) other;
		return Objects.equal(viewName, view.viewName)
				&& Objects.equal(mapFunction, view.mapFunction);

	}


	@Override
	public int hashCode() {
		return Objects.hashCode(viewName, mapFunction);
	}

}
