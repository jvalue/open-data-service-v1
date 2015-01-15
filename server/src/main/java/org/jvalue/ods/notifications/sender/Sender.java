package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.notifications.DataSink;


public abstract class Sender<T extends Client> implements DataSink {

	protected final T client;
	private SenderResult senderResult;

	protected Sender(T client) {
		this.client = client;
	}


	public final SenderResult getSenderResult() {
		return senderResult;
	}


	protected final void setSuccessResult() {
		senderResult = new SenderResult.Builder(SenderResult.Status.SUCCESS).build();
	}


	protected final void setErrorResult(String msg) {
		senderResult = new SenderResult.Builder(SenderResult.Status.ERROR).errorMsg(msg).build();
	}


	protected final void setErrorResult(Throwable cause) {
		senderResult = new SenderResult.Builder(SenderResult.Status.ERROR).errorCause(cause).build();
	}

	
	protected final void setUpdateClientResult(Client oldClient, Client newClient) {
		senderResult = new SenderResult.Builder(SenderResult.Status.UPDATE_CLIENT)
			.oldClient(oldClient)
			.newClient(newClient)
			.build();
	}


	protected final void setRemoveClientResult(Client oldClient) {
		senderResult = new SenderResult.Builder(SenderResult.Status.REMOVE_CLIENT)
			.oldClient(oldClient)
			.build();
	}

}
