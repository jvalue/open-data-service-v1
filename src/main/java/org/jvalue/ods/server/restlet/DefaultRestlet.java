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

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.server.utils.RestletResult;
import org.restlet.Request;

import com.fasterxml.jackson.databind.JsonNode;


public class DefaultRestlet extends BaseRestlet {

	@Override
	protected RestletResult doGet(Request request) {
		String url = request.getResourceRef().toString();

		String[] tree = url.split("/");

		if (url.contains("/ods/$")) {

			DbAccessor<JsonNode> dbAccessor = DbFactory.createDbAccessor("ods");
			dbAccessor.connect();

			// determine position where intern path begins
			int pos = 0;
			for (int i = 0; i < tree.length; i++) {
				if (tree[i].startsWith("$")) {
					pos = i;
					break;
				}
			}

			JsonNode node = null;
			String objectId = tree[pos].substring(1);
			try {
				node = dbAccessor.getDocument(JsonNode.class, objectId);
				for (int k = pos + 1; k < tree.length; k++) {
					node = node.get(tree[k]);
				}
				return RestletResult.newSuccessResult(node);

			} catch (RuntimeException e) {
				return onBadRequest("no data for given id '" + objectId + "'");
			}
		}

		return onBadRequest("Invalid query, see /api for available queries");
	}

}
