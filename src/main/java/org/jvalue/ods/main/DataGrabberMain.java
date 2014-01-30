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
package org.jvalue.ods.main;

import java.io.IOException;

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.GenericValue;
import org.jvalue.ods.data.ListValue;
import org.jvalue.ods.data.pegelonline.PegelOnlineMetaData;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.grabber.pegelonline.PegelOnlineGrabber;
import org.jvalue.ods.logger.Logging;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * The Class DataGrabberMain.
 */
public class DataGrabberMain {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws JsonParseException,
			JsonMappingException, IOException {
		insertPegelOnlineStationsIntoDatabase();
	}

	/**
	 * Insert pegel online stations into database.
	 * 
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void insertPegelOnlineStationsIntoDatabase()
			throws JsonParseException, JsonMappingException, IOException {
		PegelOnlineGrabber pegelOnlineAdapter = new PegelOnlineGrabber();

		// generic import
		ListValue list = pegelOnlineAdapter.getPegelOnlineStationsGeneric();
		DbAccessor a = DbFactory.createCouchDbAccessor("pegelonline");
		a.connect();
		a.deleteDatabase();
		a.insert(new PegelOnlineMetaData());
		for (GenericValue gv : list.getList()) {
			a.insert(gv);
		}

		//ToDo: IdPaths for createDesignDocument in a map, to have unique ids?
		DesignDocument dd = createDesignDocument("_design/pegelonline", "function(doc) { if(doc.longname) emit( doc.longname, null)}");
		try {
			a.insert(dd);
		} catch (UpdateConflictException ex) {
			System.err.println("Design Document already exists."  + ex.getMessage());
			Logging.error(DataGrabberMain.class, "Design Document already exists." + ex.getMessage());
		}

	}

	/**
	 * Creates the pegel online design document.
	 */
	public static DesignDocument createDesignDocument(String idPath, String function) {
		DesignDocumentFactory fac = new StdDesignDocumentFactory();
		DesignDocument dd = fac.newDesignDocumentInstance();
		dd.setId(idPath);
		View v = new DesignDocument.View();		
		v.setMap(function);
		dd.addView("getSingleStation", v);

		return dd;
		
	}
}
