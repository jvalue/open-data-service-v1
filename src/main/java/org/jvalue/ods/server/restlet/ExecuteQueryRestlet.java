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

import java.io.IOException;
import java.util.List;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.logger.Logging;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class ClassObjectRestlet.
 */
public class ExecuteQueryRestlet extends Restlet {

	private final DbAccessor<JsonNode> dbAccessor;
	private final String designDocId;
	private final String viewName;
	private final boolean fetchAllDbEntries;
	private final String customErrorMsg;
	private final ObjectMapper mapper = new ObjectMapper();


	private ExecuteQueryRestlet(
			DbAccessor<JsonNode> dbAccessor,
			String designDocId, 
			String viewName,
			boolean fetchAllDbEntries,
			String customErrorMsg) {

		this.dbAccessor = dbAccessor;
		this.designDocId = designDocId;
		this.viewName = viewName;
		this.fetchAllDbEntries = fetchAllDbEntries;
		this.customErrorMsg = customErrorMsg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.Restlet#handle(org.restlet.Request,
	 * org.restlet.Response)
	 */
	@Override
	public void handle(Request request, Response response) {
		String message =  null;
		try {
			try {
				dbAccessor.connect();

				List<JsonNode> nodes = dbAccessor.executeDocumentQuery(
						designDocId, 
						viewName, 
						null);

				if (fetchAllDbEntries) message = mapper.writeValueAsString(nodes);
				else message = mapper.writeValueAsString(nodes.get(0));

			} catch (RuntimeException e) {
				String errorMessage = "Could not retrieve data from db: " + e;
				Logging.error(this.getClass(), errorMessage);
				System.err.println(errorMessage);
				message = customErrorMsg;
			}

		} catch (IOException e) {
			String errorMessage = "Error during client request: " + e;
			Logging.error(this.getClass(), errorMessage);
			System.err.println(errorMessage);
		}

		response.setEntity(message, MediaType.APPLICATION_JSON);
	}


	public static final class Builder {
		
		private final DbAccessor<JsonNode> dbAccessor;
		private final String designDocId, viewName;
		private boolean fetchAllDbEntries = true;
		private String customErrorMsg = "Could not retrieve data.";

		public Builder(DbAccessor<JsonNode> dbAccessor, String designDocId, String viewName) {
			if (dbAccessor == null || designDocId == null || viewName == null)
				throw new NullPointerException("params cannot be null");
			this.dbAccessor = dbAccessor;
			this.designDocId = designDocId;
			this.viewName = viewName;
		}


		/** Whether all entries should be fetched from the db. If false only
		* the first entry will be fetched. */
		public Builder fetchAllDbEntries(boolean fetchAllDbEntries) {
			this.fetchAllDbEntries = fetchAllDbEntries;
			return this;
		}

		public Builder customErrorMsg(String customErrorMsg) {
			if (customErrorMsg == null) throw new NullPointerException("param cannot be null");
			this.customErrorMsg = customErrorMsg;
			return this;
		}

		public ExecuteQueryRestlet build() {
			return new ExecuteQueryRestlet(
					dbAccessor,
					designDocId,
					viewName,
					fetchAllDbEntries,
					customErrorMsg);
		}
	}
}
