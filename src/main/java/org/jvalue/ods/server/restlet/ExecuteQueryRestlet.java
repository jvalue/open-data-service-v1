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
package org.jvalue.ods.server.restlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.server.utils.RestletResult;
import org.restlet.Request;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExecuteQueryRestlet extends BaseRestlet {

	private final DbAccessor<JsonNode> dbAccessor;
	private final String designDocId;
	private final String viewName;
	private final boolean fetchAllDbEntries;
	private final String attributeName;
	private final ObjectMapper mapper = new ObjectMapper();

	private ExecuteQueryRestlet(DbAccessor<JsonNode> dbAccessor,
			String designDocId, String viewName, boolean fetchAllDbEntries,
			String attributeName) {

		this.dbAccessor = dbAccessor;
		this.designDocId = designDocId;
		this.viewName = viewName;
		this.fetchAllDbEntries = fetchAllDbEntries;
		this.attributeName = attributeName;
	}

	@Override
	protected RestletResult doGet(Request request) {
		dbAccessor.connect();

		String attributeValue = null;
		if (attributeName != null) {
			try {
				attributeValue = URLDecoder.decode(
						request.getAttributes().get(attributeName).toString(),
						"UTF-8");
			} catch (UnsupportedEncodingException uee) {
				throw new RuntimeException(uee);
			}
		}

		List<JsonNode> nodes = dbAccessor.executeDocumentQuery(designDocId,
				viewName, attributeValue);

		JsonNode resultData;

		if (fetchAllDbEntries) {
			resultData = mapper.valueToTree(nodes);
		} else if (!nodes.isEmpty()) {
			resultData = nodes.get(0);
		} else {
			return RestletResult.newErrorResult(Status.CLIENT_ERROR_NOT_FOUND,
					"No data found.");
		}

		return RestletResult.newSuccessResult(resultData);
	}

	public static final class Builder {

		private final DbAccessor<JsonNode> dbAccessor;
		private final String designDocId, viewName;
		private boolean fetchAllDbEntries = true;
		private String attributeName = null;

		public Builder(DbAccessor<JsonNode> dbAccessor, String designDocId,
				String viewName) {
			if (dbAccessor == null || designDocId == null || viewName == null)
				throw new NullPointerException("params cannot be null");
			this.dbAccessor = dbAccessor;
			this.designDocId = designDocId;
			this.viewName = viewName;
		}

		/**
		 * Whether all entries should be fetched from the db. If false only the
		 * first entry will be fetched.
		 */
		public Builder fetchAllDbEntries(boolean fetchAllDbEntries) {
			this.fetchAllDbEntries = fetchAllDbEntries;
			return this;
		}

		/**
		 * Set this value if you wish to specify a attribute value to be used
		 * during querying.
		 */
		public Builder attributeName(String attributeName) {
			this.attributeName = attributeName;
			return this;
		}

		public ExecuteQueryRestlet build() {
			return new ExecuteQueryRestlet(dbAccessor, designDocId, viewName,
					fetchAllDbEntries, attributeName);
		}
	}
}
