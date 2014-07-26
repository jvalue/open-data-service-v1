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
package integration.org.jvalue.ods.translator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jvalue.ods.data.DummyDataSource;
import org.jvalue.ods.data.generic.GenericEntity;
import org.jvalue.ods.grabber.GrabberFactory;
import org.jvalue.ods.translator.TranslatorFactory;
import org.w3c.dom.Document;


public class XmlTranslatorTest {

	@Test
	public void testTranslate() {
		Document xmlDocument = GrabberFactory.getXmlGrabber(DummyDataSource.newInstance("osm", "/nbgcity.osm")).filter(null);
		GenericEntity gv = TranslatorFactory.getXmlTranslator().filter(xmlDocument);		
		assertNotNull(gv);
	}


	@Test
	public void testTranslateNotExistingFile() {
		Document xmlDocument = GrabberFactory.getXmlGrabber(DummyDataSource.newInstance("osm", "NotExistingFile")).filter(null);		
		assertNull(xmlDocument);
	}

}
