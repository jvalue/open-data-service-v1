/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter;


public final class SourceAdapterException extends RuntimeException {

	public SourceAdapterException(String message) {
		super(message);
	}

	public SourceAdapterException(Throwable cause) {
		super(cause);
	}

	public SourceAdapterException(String message, Throwable cause) {
		super(message, cause);
	}

}
