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
import org.jvalue.ods.server.utils.RestletResult;
import org.restlet.Request;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;


public class AccessObjectByIdRestlet extends BaseRestlet {

	private DbAccessor<JsonNode> dbAccessor;


	public AccessObjectByIdRestlet(DbAccessor<JsonNode> dbAccessor) {
		this.dbAccessor = dbAccessor;
	}


	@Override
	protected RestletResult doGet(Request request) {
		dbAccessor.connect();
		String id = (String) request.getAttributes().get("id");
		try {

			JsonNode n = dbAccessor.getDocument(JsonNode.class, id);
			return RestletResult.newSuccessResult(n);

		} catch (RuntimeException e) {
			return RestletResult.newErrorResult(
					Status.CLIENT_ERROR_NOT_FOUND, 
					"No data found for id '" + id + "'");
		}

	}
}
