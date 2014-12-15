/*  Open Data Service
    Copyright (C)

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
package org.jvalue.ods.qa;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.objecttypes.MapObjectType;
import org.jvalue.ods.filter.Filter;
import org.jvalue.ods.utils.Log;

import java.util.List;
import java.util.Map;

/**
 * The Class DataAdditionFilter.
 */
public class DataAdditionFilter extends Filter<Object, Object> {

	/** The source. */
	private DataSource source;

	/**
	 * Instantiates a new data addition filter.
	 * 
	 * @param source
	 *            the source
	 */
	public DataAdditionFilter(DataSource source) {
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.filter.Filter#filter(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object doProcess(Object object) {

		try {

			if (object instanceof List) {
				List<Object> list = (List<Object>) object;

				for (Object o : list) {
					Map<String, Object> map = (Map<String, Object>) o;
					MapObjectType schema = (MapObjectType) source
							.getImprovedDbSchema();

					MapObjectType objectType = (MapObjectType) schema
							.getReferencedObjects().get("objectType");
					String s = (String) objectType.getAttributes().keySet()
							.toArray()[0];

					map.put("dataType", s);

					if (!map.containsKey("dataQualityStatus")) {
						map.put("dataQualityStatus", "raw");
					}
				}

			}
		} catch (Exception e) {
			Log.error(e.getMessage()
					+ "Mandatory DataAdditionFilter failed");
			System.err.println(e.getMessage()
					+ "Mandatory DataAdditionFilter failed");
			throw e;
		}

		return object;
	}


	@Override
	protected void doOnComplete() {
		// nothing to do here
	}

}
