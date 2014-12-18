package org.jvalue.ods.processor.adapter.server;


import java.net.URL;

public interface Server {

	public void start(String content) throws Exception;
	public void stop() throws Exception;
	public URL getFileUrl() throws Exception;

}
