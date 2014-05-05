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
package org.jvalue.ods.administration;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.Router;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class PoiRouter.
 */
public class AdministrationRouter implements Router<Restlet> {

	/** The routes. */
	private HashMap<String, Restlet> routes;

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/**
	 * Instantiates a new routes router.
	 */
	public AdministrationRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("adminlog");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.adapter.RouterInterface#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {
		routes = new HashMap<String, Restlet>();

		Restlet adminLogRestlet = new Restlet() {

			@Override
			public void handle(Request request, Response response) {

				String message = "";
				List<String> logs = new LinkedList<String>();
				try {
					dbAccessor.connect();

					List<JsonNode> nodes = dbAccessor.getAllDocuments();
					for (JsonNode n : nodes) {
						logs.add(n.get("log").asText());
					}
					Collections.sort(logs);

					for (String s : logs) {
						message += s;
					}

				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					Logging.error(this.getClass(), ex.getMessage());
				}

				response.setEntity(message, MediaType.TEXT_PLAIN);

			}
		};

		routes.put("/admin/logs", adminLogRestlet);

		return routes;
	}

	/**
	 * Gets the db accessor.
	 * 
	 * @return the db accessor
	 */
	public DbAccessor<JsonNode> getDbAccessor() {
		return dbAccessor;
	}

	/**
	 * Sets the db accessor.
	 * 
	 * @param dbAccessor
	 *            the new db accessor
	 */
	public void setDbAccessor(DbAccessor<JsonNode> dbAccessor) {
		this.dbAccessor = dbAccessor;
	}

}
