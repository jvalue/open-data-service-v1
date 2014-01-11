/*
    Open Data Service
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

package org.jvalue.ods.main;

import java.util.HashMap;

import org.jvalue.ods.adapter.ApiRouter;
import org.jvalue.ods.adapter.pegelonline.PegelOnlineRouter;
import org.jvalue.ods.restlet.RestletAdapter;
import org.restlet.Restlet;

/**
 * The Class Main.
 */
public class Main {

	/** The Constant port. */
	private final static int port = 8182;
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		PegelOnlineRouter poRouter = new PegelOnlineRouter();

		HashMap<String, Restlet> combinedRouter = new HashMap<String, Restlet>();
		combinedRouter.putAll(poRouter.getRoutes());

		// last router, generates api output
		ApiRouter router = new ApiRouter(combinedRouter);

		combinedRouter.putAll(router.getRoutes());

		new RestletAdapter(combinedRouter, port);
	}

}
