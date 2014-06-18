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
package org.jvalue.ods.grabber;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.data.DataSourceVisitor;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.sources.OsmSource;
import org.jvalue.ods.data.sources.PegelOnlineSource;
import org.jvalue.ods.data.sources.PegelPortalMvSource;
import org.jvalue.ods.translator.JsonTranslator;
import org.jvalue.ods.translator.OsmTranslator;
import org.jvalue.ods.translator.PegelPortalMvTranslator;


public final class GrabberVisitor implements DataSourceVisitor<Void, GenericEntity> {

	@Override
	public GenericEntity visit(PegelOnlineSource source, Void param) {
		return handle(JsonTranslator.INSTANCE, source);
	}


	@Override
	public GenericEntity visit(PegelPortalMvSource source, Void param) {
		return handle(PegelPortalMvTranslator.INSTANCE, source);
	}


	@Override
	public GenericEntity visit(OsmSource source, Void param) {
		return handle(OsmTranslator.INSTANCE, source);
	}


	private GenericEntity handle(Translator translator, DataSource source) {
		return translator.translate(source);
	}

}
