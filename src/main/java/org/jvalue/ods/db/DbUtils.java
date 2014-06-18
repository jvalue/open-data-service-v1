/*  Open Data Service

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

import org.ektorp.UpdateConflictException;
import org.ektorp.support.DesignDocument;
import org.ektorp.support.DesignDocument.View;
import org.ektorp.support.DesignDocumentFactory;
import org.ektorp.support.StdDesignDocumentFactory;
import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.db.exception.DbException;
import org.jvalue.ods.logger.Logging;

import com.fasterxml.jackson.databind.JsonNode;


public final class DbUtils {
	
	private static final DesignDocumentFactory designFactory = new StdDesignDocumentFactory();

	public static void createView(final DbAccessor<JsonNode> accessor, final OdsView view) {
		if (accessor == null || view == null) 
			throw new NullPointerException("params cannot be null");

		DesignDocument document = null;
		boolean update = true;

		try {
			document = accessor.getDocument(DesignDocument.class, view.getIdPath());
		} catch (DbException e) {
			document = designFactory.newDesignDocumentInstance();
			document.setId(view.getIdPath());
			update = false;
		}

		View v = new DesignDocument.View();
		v.setMap(view.getFunction());
		document.addView(view.getViewName(), v);

		try {
			if (update) accessor.update(document);
			else accessor.insert(document);

		} catch (UpdateConflictException ex) {
			System.err.println("Design Document already exists." + ex.getMessage());
			Logging.error(DbUtils.class, "Design Document already exists." + ex.getMessage());
		}
	}


	private DbUtils() { }

}
