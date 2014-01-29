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
package org.jvalue.ods.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Logging.
 */
public abstract class Logging {

	/**
	 * Creates the logger.
	 * 
	 * @param c
	 *            the c
	 * @return the logger
	 */
	private static Logger createLogger(Class<?> c) {
		return LoggerFactory.getLogger(c);
	}

	/**
	 * Info.
	 * 
	 * @param c
	 *            the c
	 * @param s
	 *            the s
	 */
	public static void info(Class<?> c, String s) {
		Logger log = createLogger(c);
		log.info(s);
	}

	/**
	 * Error.
	 * 
	 * @param c
	 *            the c
	 * @param s
	 *            the s
	 */
	public static void error(Class<?> c, String s) {
		Logger log = createLogger(c);
		log.error(s);
	}

	/**
	 * Debug.
	 * 
	 * @param c
	 *            the c
	 * @param s
	 *            the s
	 */
	public static void debug(Class<?> c, String s) {
		Logger log = createLogger(c);
		log.debug(s);
	}

}
