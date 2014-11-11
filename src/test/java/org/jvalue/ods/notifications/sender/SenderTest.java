package org.jvalue.ods.notifications.sender; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public final class SenderTest {

	@Test
	public final void testSuccessResult() {

		DummySender sender = new DummySender();

		SenderResult success = sender.getSuccessResult();
		assertNotNull(success);
		assertEquals(success.getStatus(), SenderResult.Status.SUCCESS);
		assertNull(success.getOldClient());
		assertNull(success.getNewClient());
		assertNull(success.getErrorMsg());
		assertNull(success.getErrorCause());

	}


	@Test
	public final void testErrorMsgResult() {

		DummySender sender = new DummySender();

		SenderResult error = sender.getErrorResult("error");
		assertNotNull(error);
		assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		assertEquals(error.getErrorMsg(), "error");
		assertNull(error.getOldClient());
		assertNull(error.getNewClient());
		assertNull(error.getErrorCause());

	}


	@Test
	public final void testErrorCauseResult() {

		DummySender sender = new DummySender();
		Exception exception = new RuntimeException("error");

		SenderResult error = sender.getErrorResult(exception);
		assertNotNull(error);
		assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		assertEquals(error.getErrorCause(), exception);
		assertNull(error.getOldClient());
		assertNull(error.getNewClient());
		assertNull(error.getErrorMsg());

	}

}
