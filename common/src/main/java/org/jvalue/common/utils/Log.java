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
package org.jvalue.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Log {

	private Log() { }


	public static void debug(String msg) {
		getLogger().debug(msg);
	}


	public static void debug(String msg, Throwable throwable) {
		getLogger().debug(msg, throwable);
	}


	public static void info(String msg) {
		getLogger().info(msg);
	}


	public static void info(String msg, Throwable throwable) {
		getLogger().info(msg, throwable);
	}


	public static void warn(String msg) {
		getLogger().warn(msg);
	}


	public static void warn(String msg, Throwable throwable) {
		getLogger().warn(msg, throwable);
	}


	public static void error(String msg) {
		getLogger().error(msg);
	}


	public static void error(String msg, Throwable throwable) {
		getLogger().error(msg, throwable);
	}


	private static Logger getLogger() {
		StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
		return LoggerFactory.getLogger(stackElements[3].getClassName());
	}

}
