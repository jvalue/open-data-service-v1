package org.jvalue.ods.processor.adapter.server;


import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.net.URL;


public final class FtpServer implements Server {

	private static final String FILE_NAME = FtpServer.class.getSimpleName();


	private FakeFtpServer ftpServer;

	@Override
	public void start(String content, int port) {
		ftpServer = new FakeFtpServer();
		ftpServer.setServerControlPort(8083);
		ftpServer.addUserAccount(new UserAccount("user", "pass", "/"));
		FileSystem fileSystem = new UnixFakeFileSystem();
		fileSystem.add(new DirectoryEntry("/"));
		fileSystem.add(new FileEntry("/" + FILE_NAME, content));
		ftpServer.setFileSystem(fileSystem);
		ftpServer.start();
	}


	@Override
	public void stop() {
		ftpServer.stop();
		ftpServer = null;
	}


	@Override
	public URL getFileUrl() throws Exception {
		return new URL("ftp://user:pass@localhost:8083/" + FILE_NAME);
	}

}
