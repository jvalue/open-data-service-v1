package org.jvalue.ods.notifications.sender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.notifications.clients.DummyClient;


public final class SenderResultTest {

	@Test
	public void testEmptyBuild() {

		SenderResult result = new SenderResult.Builder(SenderResult.Status.UPDATE_CLIENT).build();
		assertEquals(result.getStatus(), SenderResult.Status.UPDATE_CLIENT);
		assertNull(result.getOldClient());
		assertNull(result.getNewClient());
		assertNull(result.getErrorCause());
		assertNull(result.getErrorMsg());

	}


	@Test
	public void testFullBuild() {

		Throwable throwable  = new RuntimeException("bang");
		String errorMsg = "error";
		Client oldClient = new DummyClient("dummy1", "dummy1");
		Client newClient = new DummyClient("dummy2", "dummy2");

		SenderResult result = new SenderResult.Builder(SenderResult.Status.REMOVE_CLIENT)
			.oldClient(oldClient)
			.newClient(newClient)
			.errorCause(throwable)
			.errorMsg(errorMsg)
			.build();

		assertEquals(result.getStatus(), SenderResult.Status.REMOVE_CLIENT);
		assertEquals(result.getOldClient(), oldClient);
		assertEquals(result.getNewClient(), newClient);
		assertEquals(result.getErrorCause(), throwable);
		assertEquals(result.getErrorMsg(), errorMsg);

	}

}
