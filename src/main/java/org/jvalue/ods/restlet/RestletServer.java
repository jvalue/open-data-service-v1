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
package org.jvalue.ods.restlet;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

/**
 * The Class RestletAdapter.
 */
public class RestletServer extends Application {

	// Collection of routes for Restlet
	/** The routes. */
	private Map<String, Restlet> routes;

	/** The port. */
	private int port;

	/** The component. */
	private Component component;

	/**
	 * Instantiates a new restlet adapter.
	 * 
	 * @param routes
	 *            the routes
	 * @param port
	 *            the port
	 * @throws Exception
	 *             the exception
	 */
	public RestletServer(Map<String, Restlet> routes, int port)
			throws Exception {
		if (routes == null || routes.isEmpty())
			throw new IllegalArgumentException("routes are null or empty");

		if (port < 1024 || port > 49151)
			throw new IllegalArgumentException(
					"port not between 1024 and 49151");

		this.routes = routes;
		this.port = port;
	}

	/**
	 * Initialize.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void initialize() throws Exception {
		component = new Component();
		component.getServers().add(Protocol.HTTP, port);

		Application app = this;

		component.getDefaultHost().attach(app);
		component.start();
	}

	/**
	 * Disconnect.
	 * 
	 */
	public void disconnect() {
		try {
			component.stop();
		} catch (Exception e) {
			System.err.println("Component could not be stopped.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public Restlet createInboundRoot() {
		// Create a root router
		Router router = new Router(getContext());

		// Get Map in Set interface to get key and value
		Set<Entry<String, Restlet>> s = routes.entrySet();

		// Move next key and value of Map by iterator
		Iterator<Entry<String, Restlet>> it = s.iterator();

		while (it.hasNext()) {
			Entry<String, Restlet> m = it.next();
			router.attach(m.getKey(), m.getValue());
		}

		return router;
	}

}
