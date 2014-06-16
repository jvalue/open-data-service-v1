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
package org.jvalue.ods.db;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.generic.MapObject;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.filter.OdsFilter;
import org.jvalue.ods.logger.Logging;
import org.jvalue.ods.main.DataGrabberMain;

import com.fasterxml.jackson.databind.JsonNode;


public final class DbInsertionFilter extends OdsFilter<GenericEntity, Void> {

	private final DbAccessor<JsonNode> accessor;

	public DbInsertionFilter(DbAccessor<JsonNode> accessor) {
		if (accessor == null) throw new NullPointerException("param cannot be null");
		this.accessor = accessor;
	}


	@Override
	protected Void filterHelper(DataSource source, GenericEntity data) {
		insertData(source, data);
		insertViews(source);
		accessor.insert(source.getMetaData());
		return null;
	}


	private void insertData(DataSource source, GenericEntity data) {
		if (data instanceof ListObject) {

			ListObject lv = (ListObject) data;
			List<MapObject> list = new LinkedList<>();

			for (Serializable i : lv.getList()) {
				list.add((MapObject) i);
			}

			accessor.executeBulk(list, source.getDbSchema());
		} else if (data instanceof MapObject) {
			LinkedList<MapObject> list = new LinkedList<>();
			list.add((MapObject) data);
			accessor.executeBulk(list, source.getDbSchema());
		} else {
			String errmsg = "GenericValue must be ListValue or MapValue.";
			System.err.println(errmsg);
			Logging.error(DataGrabberMain.class, errmsg);
			throw new RuntimeException(errmsg);
		}
	}


	private void insertViews(DataSource source) {
		for (OdsView view : source.getOdsViews()) {
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
}
