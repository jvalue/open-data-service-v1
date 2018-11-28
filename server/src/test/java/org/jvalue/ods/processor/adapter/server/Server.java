package org.jvalue.ods.processor.adapter.server;


import java.net.URL;

public interface Server {

	public void start(String content, int port) throws Exception;
	public void stop() throws Exception;
	public URL getFileUrl() throws Exception;

}
