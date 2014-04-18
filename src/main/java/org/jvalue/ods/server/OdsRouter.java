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
package org.jvalue.ods.server;

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.main.Router;
import org.jvalue.ods.server.restlet.AccessObjectByIdRestlet;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class OdsRouter.
 */
public class OdsRouter implements Router<Restlet> {
	/** The routes. */
	private HashMap<String, Restlet> routes;

	/** The db accessor. */
	private DbAccessor<JsonNode> dbAccessor;

	/**
	 * Instantiates a new pegel online router.
	 * 
	 */
	public OdsRouter() {
		this.dbAccessor = DbFactory.createDbAccessor("ods");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jvalue.ods.main.Router#getRoutes()
	 */
	@Override
	public Map<String, Restlet> getRoutes() {

		routes = new HashMap<String, Restlet>();

		Restlet odsRestlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {

				String message = "Please specify an argument.";

				// there is an attribute in url
				if (request.getResourceRef().getQueryAsForm().size() == 1) {

					message = new RouterUtils().getDocumentByAttribute(request,
							dbAccessor);

				}

				response.setEntity(message, MediaType.APPLICATION_JSON);

			}
		};

		routes.put("/ods/${id}", new AccessObjectByIdRestlet(dbAccessor));
		routes.put("/ods", odsRestlet);

		return routes;
	}

}
