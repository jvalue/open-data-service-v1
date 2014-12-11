package org.jvalue.ods.filter.adapter;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.data.DataSource;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.net.URL;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class JsonSourceAdapterTest {

	private static final String FILE_NAME = JsonSourceAdapterTest.class.getSimpleName() + ".json";
	private static final ArrayNode JSON_ARRAY;
	static {
		JSON_ARRAY = new ArrayNode(JsonNodeFactory.instance);
		ObjectNode jsonObject = new ObjectNode(JsonNodeFactory.instance);
		jsonObject.put("key1", "value1");
		jsonObject.put("key2", "value2");
		JSON_ARRAY.add(jsonObject);
	}

	private FakeFtpServer ftpServer;


	@Before
	public void setupFtpServer() throws Exception {
		ftpServer = new FakeFtpServer();
		ftpServer.setServerControlPort(8083);
		ftpServer.addUserAccount(new UserAccount("user", "pass", "/"));
		FileSystem fileSystem = new UnixFakeFileSystem();
		fileSystem.add(new DirectoryEntry("/"));
		fileSystem.add(new FileEntry("/" + FILE_NAME, JSON_ARRAY.toString()));
		ftpServer.setFileSystem(fileSystem);
		ftpServer.start();
	}


	@After
	public void tearDownFtpServer() {
		ftpServer.stop();
	}


	@Test
	public void testFtpSource(@Mocked final DataSource source) throws Exception {
		new Expectations() {{
			source.getUrl();
			result = new URL("ftp://user:pass@localhost:8083/" + FILE_NAME);
		}};

		JsonSourceAdapter adapter = new JsonSourceAdapter(source);
		ArrayNode resultJsonArray = adapter.grabSource();
		Assert.assertEquals(JSON_ARRAY, resultJsonArray);
	}

}
