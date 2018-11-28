package org.jvalue.ods.processor.adapter.server;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;


public final class HttpServer implements Server {

	private static final String FILE_NAME = HttpServer.class.getSimpleName();


	private com.sun.net.httpserver.HttpServer httpServer;

	private int port;


	@Override
	public void start(String content, int port) throws Exception {
		if (port == 0) this.port = 8083;
		else this.port = port;
		httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(this.port), 0);
		httpServer.createContext("/" + FILE_NAME, new FileHandler(content));
		httpServer.setExecutor(null);
		httpServer.start();
	}


	@Override
	public void stop() {
		httpServer.stop(0);
		httpServer = null;
	}


	@Override
	public URL getFileUrl() throws Exception {
		return new URL("http://localhost:"+port+"/" + FILE_NAME);
	}


	private static class FileHandler implements HttpHandler {

		private final String content;


		public FileHandler(String content) {
			this.content = content;

		}


		@Override
		public void handle(HttpExchange exchange) {
			try {
				exchange.sendResponseHeaders(200, content.length());
				OutputStream outputStream = exchange.getResponseBody();
				outputStream.write(content.getBytes());
				outputStream.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}

}
