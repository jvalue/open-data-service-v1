/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.filter;


public class FilterException extends Exception {

	public FilterException(String message) {
		super(message);
	}


	public FilterException(String message, Throwable cause) {
		super(message, cause);
	}


	public FilterException(Throwable cause) {
		super(cause);
	}

}
