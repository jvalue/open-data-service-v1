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

import java.util.List;

import org.jvalue.ods.data.OdsView;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.data.generic.ListObject;
import org.jvalue.ods.data.metadata.OdsMetaData;
import org.jvalue.ods.data.objecttypes.ObjectType;
import org.jvalue.ods.data.sources.PegelPortalMvSource;
import org.jvalue.ods.main.Grabber;
import org.jvalue.ods.translator.PegelPortalMvTranslator;

/**
 * The Class MecklenburgVorpommernPegel
 */
public class PegelPortalMvGrabber implements Grabber {

	@Override
	public GenericEntity grab() {
		return (ListObject) new PegelPortalMvTranslator().translate(PegelPortalMvSource.createInstance());
	}

	@Override
	public ObjectType getDataSourceSchema() {
		return PegelPortalMvSource.createInstance().getDataSourceSchema();
	}

	@Override
	public ObjectType getDbSchema() {
		return PegelPortalMvSource.createInstance().getDbSchema();
	}

	@Override
	public OdsMetaData getMetaData() {
		return PegelPortalMvSource.createInstance().getMetaData();
	}

	@Override
	public List<OdsView> getOdsViews() {
		return PegelPortalMvSource.createInstance().getOdsViews();
	}

}
