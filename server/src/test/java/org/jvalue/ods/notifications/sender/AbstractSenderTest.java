package org.jvalue.ods.notifications.sender;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.notifications.DummyClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public final class AbstractSenderTest {

	private final DataSource source = new DataSource("someId", null, null, null);
	private DummySender sender;

	@Before
	public void setupSender() {
		sender = new DummySender(source, new DummyClient("someId", "someSource"));
	}


	@Test
	public final void testSuccessResult() {
		sender.setSuccessResult();
		SenderResult success = sender.getSenderResult();
		assertNotNull(success);
		assertEquals(success.getStatus(), SenderResult.Status.SUCCESS);
		assertNull(success.getOldClient());
		assertNull(success.getNewClient());
		assertNull(success.getErrorMsg());
		assertNull(success.getErrorCause());
	}


	@Test
	public final void testErrorMsgResult() {
		sender.setErrorResult("error");
		SenderResult error = sender.getSenderResult();
		assertNotNull(error);
		assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		assertEquals(error.getErrorMsg(), "error");
		assertNull(error.getOldClient());
		assertNull(error.getNewClient());
		assertNull(error.getErrorCause());
	}


	@Test
	public final void testErrorCauseResult() {
		Exception exception = new RuntimeException("error");
		sender.setErrorResult(exception);
		SenderResult error = sender.getSenderResult();
		assertNotNull(error);
		assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		assertEquals(error.getErrorCause(), exception);
		assertNull(error.getOldClient());
		assertNull(error.getNewClient());
		assertNull(error.getErrorMsg());
	}

}
