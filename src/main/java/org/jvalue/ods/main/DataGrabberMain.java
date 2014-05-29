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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
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

		createCommonViews();

		Logging.adminLog("Update completed");
	}

	private static void createCommonViews() {
		createView(new OdsView("_design/ods", "getClassObjectByType",
				"function(doc) { if(doc.objectType) emit (doc.objectType, doc) }"));
		createView(new OdsView("_design/ods", "getAllClassObjects",
				"function(doc) { if(doc.objectType) emit (null, doc) }"));
	}

	/**
	 * Insert db bulk.
	 * 
	 * @param schema
	 *            the schema
	 * @param gv
	 *            the gv
	 */
	private static void insertDbBulk(MapSchema schema, GenericEntity gv) {
		if (gv instanceof ListObject) {

			ListObject lv = (ListObject) gv;
			List<MapObject> list = new LinkedList<>();

			for (Serializable i : lv.getList()) {
				list.add((MapObject) i);
			}

			accessor.executeBulk(list, schema);
		} else if (gv instanceof MapObject) {
			LinkedList<MapObject> list = new LinkedList<>();
			list.add((MapObject) gv);
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
		GenericEntity data = grabber.grab();

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
