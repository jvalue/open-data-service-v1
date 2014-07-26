package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.data.DataSource;
import org.jvalue.ods.notifications.clients.Client;


public abstract class NotificationSender<T extends Client> {
	
	public abstract SenderResult notifySourceChanged(
			T client, 
			DataSource source, 
			Object data);


	protected SenderResult getSuccessResult() {
		return new SenderResult.Builder(SenderResult.Status.SUCCESS).build();
	}


	protected SenderResult getErrorResult(String msg) {
		return new SenderResult.Builder(SenderResult.Status.ERROR).errorMsg(msg).build();
	}


	protected SenderResult getErrorResult(Throwable cause) {
		return new SenderResult.Builder(SenderResult.Status.ERROR).errorCause(cause).build();
	}

	
	protected SenderResult getUpdateClientResult(Client oldClient, Client newClient) {
		return new SenderResult.Builder(SenderResult.Status.UPDATE_CLIENT)
			.oldClient(oldClient)
			.newClient(newClient)
			.build();
	}


	protected SenderResult getRemoveClientResult(Client oldClient) {
		return new SenderResult.Builder(SenderResult.Status.REMOVE_CLIENT)
			.oldClient(oldClient)
			.build();
	}

}
