/*  Open Data Service
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
package org.jvalue.ods.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DataSourceManager {

	private final static DataSourceManager instance = new DataSourceManager();

	public static DataSourceManager getInstance() {
		return instance;
	}


	private final Map<String, DataSource> sources = new HashMap<String, DataSource>();

	private DataSourceManager() { }


	public Collection<DataSource> getAllSources() {
		return sources.values();
	}


	public Set<String> getAllIds() {
		return sources.keySet();
	}


	public DataSource getSourceForId(String id) {
		if (id == null) throw new NullPointerException("id cannot be null");
		return sources.get(id);
	}


	public void addSource(DataSource source) {
		if (source == null) throw new NullPointerException("source cannot be null");
		sources.put(source.getId(), source);
	}


	public void clearSources() {
		sources.clear();
	}

}
