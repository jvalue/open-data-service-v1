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

import org.jvalue.ods.logger.Logging;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * The Class RestletServer.
 */
public class RestletServer {

	/** The port. */
	private int port;

	/** The component. */
	private Component component;

	/**
	 * Instantiates a new restlet adapter.
	 *
	 * @param port
	 *            the port
	 * @throws Exception
	 *             the exception
	 */
	public RestletServer(int port) throws Exception {
		if (port < 1024 || port > 49151) {
			String errorMessage = "port not between 1024 and 49151";
			Logging.error(this.getClass(), errorMessage);
			throw new IllegalArgumentException(
					"port not between 1024 and 49151");
		}

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

		Application app = new ContainerRestletApp();

		component.getDefaultHost().attach(app);
		component.start();
	}

	/**
	 * Disconnect.
	 */
	public void disconnect() {
		try {
			component.stop();
		} catch (Exception e) {
			String errorMessage = "Component could not be stopped";
			Logging.error(this.getClass(), errorMessage);
			System.err.println(errorMessage);
		}
	}

}
