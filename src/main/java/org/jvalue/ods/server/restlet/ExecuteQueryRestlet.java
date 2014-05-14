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

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/** The design doc id. */
	private String designDocId;

	/** The view name. */
	private String viewName;

	/**
	 * Instantiates a new class object restlet.
	 * 
	 * @param dbAccessor
	 *            the db accessor
	 * @param designDocId
	 *            the design doc id
	 * @param viewName
	 *            the view name
	 */
	public ExecuteQueryRestlet(DbAccessor<JsonNode> dbAccessor,
			String designDocId, String viewName) {
		this.dbAccessor = dbAccessor;
		this.designDocId = designDocId;
		this.viewName = viewName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.Restlet#handle(org.restlet.Request,
	 * org.restlet.Response)
	 */
	@Override
	public void handle(Request request, Response response) {

		String message = "";
		try {

			ObjectMapper mapper = new ObjectMapper();

			try {
				dbAccessor.connect();

				List<JsonNode> n = dbAccessor.executeDocumentQuery(designDocId,
						viewName, null);

				if (n.isEmpty()) {
					throw new RuntimeException();
				} else {
					message += mapper.writeValueAsString(n.get(0));
				}

			} catch (RuntimeException e) {
				String errorMessage = "Could not retrieve data from db: " + e;
				Logging.error(this.getClass(), errorMessage);
				System.err.println(errorMessage);
				message += mapper
						.writeValueAsString("Could not retrieve data.");
			}

		} catch (IOException e) {
			String errorMessage = "Error during client request: " + e;
			Logging.error(this.getClass(), errorMessage);
			System.err.println(errorMessage);
		}

		response.setEntity(message, MediaType.APPLICATION_JSON);

	}

}
