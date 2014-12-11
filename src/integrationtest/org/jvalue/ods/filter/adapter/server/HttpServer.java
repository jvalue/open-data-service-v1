package org.jvalue.ods.filter.adapter.server;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.junit.runner.RunWith;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;

import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class HttpServer implements Server {

	private static final String FILE_NAME = HttpServer.class.getSimpleName();


	private com.sun.net.httpserver.HttpServer httpServer;

	@Override
	public void start(String content) throws Exception {
		httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(8083), 0);
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
		return new URL("http://localhost:8083/" + FILE_NAME);
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
