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

import java.util.LinkedList;
import java.util.List;

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericValue;
import org.jvalue.ods.data.generic.ListValue;
import org.jvalue.ods.data.generic.MapValue;
import org.jvalue.ods.data.schema.MapSchema;
import org.jvalue.ods.db.DbAccessor;
import org.jvalue.ods.db.DbFactory;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.grabber.OsmGrabber;
import org.jvalue.ods.grabber.PegelOnlineGrabber;
import org.jvalue.ods.logger.Logging;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The Class DataGrabberMain.
 */
public class DataGrabberMain {

	/** The accessor. */
	private static DbAccessor<JsonNode> accessor;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		Logging.adminLog("Update started");

		accessor = DbFactory.createDbAccessor("ods");
		accessor.connect();
		accessor.deleteDatabase();

		Logging.adminLog("grabbing PegelOnline...");
		grab(new PegelOnlineGrabber());

		Logging.adminLog("grabbing Osm...");
		grab(new OsmGrabber());
		insertPoiIntoDatabase();

		Logging.adminLog("Update completed");
	}

	/**
	 * Insert poi into database.
	 */
	private static void insertPoiIntoDatabase() {

		createView(new OdsView("_design/poi", "getPoiByStation",
				"function(doc) { if(doc.poi) emit (doc.longname, doc.poi) }"));
		createView(new OdsView("_design/poi", "getPoiIdByStation",
				"function(doc) { if(doc.poi) emit (doc.longname, doc._id) }"));

	}

	/**
	 * Insert db bulk.
	 * 
	 * @param schema
	 *            the schema
	 * @param gv
	 *            the gv
	 */
	private static void insertDbBulk(MapSchema schema, GenericValue gv) {
		if (gv instanceof ListValue) {

			ListValue lv = (ListValue) gv;
			List<MapValue> list = new LinkedList<>();
			for (GenericValue i : lv.getList()) {
				list.add((MapValue) i);
			}

			accessor.executeBulk(list, schema);
		} else if (gv instanceof MapValue) {
			LinkedList<MapValue> list = new LinkedList<>();
			list.add((MapValue) gv);
			accessor.executeBulk(list, schema);
		} else {
			String errmsg = "GenericValue must be ListValue or MapValue.";
			System.err.println(errmsg);
			Logging.error(DataGrabberMain.class, errmsg);
			throw new RuntimeException(errmsg);
		}
	}

	/**
	 * Grab.
	 * 
	 * @param grabber
	 *            the grabber
	 */
	private static void grab(Grabber grabber) {
		GenericValue data = grabber.grab();

		insertDbBulk(grabber.getDbSchema(), data);

		accessor.insert(grabber.getMetaData());

		for (OdsView ov : grabber.getOdsViews()) {
			createView(ov);
		}
	}

	/**
	 * Creates the pegel online design document.
	 * 
	 * @param view
	 *            the view
	 */
	private static void createView(OdsView view) {

		DesignDocument dd = null;
		boolean update = true;

		try {
			dd = accessor.getDocument(DesignDocument.class, view.getIdPath());
		} catch (DbException e) {
			DesignDocumentFactory fac = new StdDesignDocumentFactory();
			dd = fac.newDesignDocumentInstance();
			dd.setId(view.getIdPath());
			update = false;
		}

		View v = new DesignDocument.View();
		v.setMap(view.getFunction());
		dd.addView(view.getViewName(), v);

		try {
			if (update) {
				accessor.update(dd);
			} else {
				accessor.insert(dd);
			}
		} catch (UpdateConflictException ex) {
			System.err.println("Design Document already exists."
					+ ex.getMessage());
			Logging.error(DataGrabberMain.class,
					"Design Document already exists." + ex.getMessage());
		}

	}
}
