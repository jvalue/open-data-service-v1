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
package org.jvalue.common.utils;

import java.util.List;


public final class Assert {

	private Assert() { }


	public static void assertNotNull(Object... objects) {
		for (Object o : objects) {
			if (o == null) throw new NullPointerException("param is null");
		}
	}


	public static void assertTrue(boolean expression) {
		assertTrue(expression, "expression is false");
	}


	public static void assertTrue(boolean expression, String errorMsg) {
		assertFalse(!expression, errorMsg);
	}


	public static void assertFalse(boolean expression, String errorMsg) {
		if (expression) throw new IllegalArgumentException(errorMsg);
	}


	public static void assertEquals(Object o1, Object o2, String errorMsg) {
		if (o1 == null && o2 == null) return;
		if (o1 == null || o2 == null) throw new IllegalArgumentException(errorMsg);
		if (!o1.equals(o2)) throw new IllegalArgumentException(errorMsg);
	}


	public static void assertValidIdx(List<?> list, int idx) {
		if (idx < 0 || idx >= list.size()) 
			throw new IndexOutOfBoundsException("idx was " + idx + " but size is " + list.size());
	}

}
