/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.processor.adapter.server;


import java.net.URL;

public interface Server {

	public void start(String content) throws Exception;
	public void stop() throws Exception;
	public URL getFileUrl() throws Exception;

}
