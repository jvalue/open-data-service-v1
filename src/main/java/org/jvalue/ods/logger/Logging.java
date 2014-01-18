package org.jvalue.ods.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Logging {


	private static Logger createLogger(Class<?> c) {
		return LoggerFactory.getLogger(c);
	}
	
	
	public static void info(Class<?> c, String s) {		
		Logger log = createLogger(c);
		log.info(s);
	}
	

	public static void error(Class<?> c, String s) {		
		Logger log = createLogger(c);
		log.error(s);
	}
	
	public static void debug(Class<?> c, String s){		
		Logger log = createLogger(c);
		log.debug(s);
	}
	
	
	
}
