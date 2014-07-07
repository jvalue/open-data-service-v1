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
package org.jvalue.ods.translator;


public final class TranslatorFactory {

	private TranslatorFactory() { }


	private static Translator 
		jsonTranslator,
		xmlTranslator,
		osmTranslator,
		pegelPortalMvTranslator;


	public static Translator getPegelOnlineTranslator() {
		return getJsonTranslator();
	}


	public static Translator getOsmTranslator() {
		if (osmTranslator == null) osmTranslator = new OsmTranslator();
		return osmTranslator;
	}


	public static Translator getPegelPortalMvTranslator() {
		if (pegelPortalMvTranslator == null) pegelPortalMvTranslator = new PegelPortalMvTranslator();
		return pegelPortalMvTranslator;
	}


	public static Translator getXmlTranslator() {
		if (xmlTranslator == null) xmlTranslator = new XmlTranslator();
		return xmlTranslator;
	}


	public static Translator getJsonTranslator() {
		if (jsonTranslator == null) jsonTranslator = new JsonTranslator();
		return jsonTranslator;
	}

}
