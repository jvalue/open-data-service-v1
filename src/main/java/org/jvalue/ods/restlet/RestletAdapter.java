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
public class RestletAdapter extends Application {
	
	//Collection of routes for Restlet
	/** The routes. */
	private static Map<String, Restlet> routes;
    
    
	/**
	 * Inits the restlet.
	 *
	 * @param routesCollection the routes collection
	 * @throws Exception the exception
	 */
	public static void initRestlet(Map<String, Restlet> routesCollection) throws Exception {
		routes = routesCollection;		
		initialize();		
	}
	
	/**
	 * Initialize.
	 *
	 * @throws Exception the exception
	 */
	private static void initialize() throws Exception {
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 8182);

		Application app = new RestletAdapter();

		component.getDefaultHost().attach(app);
		component.start();		
	}

	/* (non-Javadoc)
	 * @see org.restlet.Application#createInboundRoot()
	 */
	@Override
	public Restlet createInboundRoot() {
		// Create a root router
		Router router = new Router(getContext());
		
		//Get Map in Set interface to get key and value		
        Set<Entry<String, Restlet>> s=routes.entrySet();
        
        //Move next key and value of Map by iterator
        Iterator<Entry<String, Restlet>> it=s.iterator();

        while(it.hasNext())
        {
        	Entry<String, Restlet> m =it.next();        	
        	router.attach(m.getKey(), m.getValue());    		
        }	
        
        return router;
	}
	
}
