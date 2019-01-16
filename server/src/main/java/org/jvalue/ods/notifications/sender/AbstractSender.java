/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;

import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.sources.DataSource;


abstract class AbstractSender<T extends Client> implements Sender<T> {

	protected final DataSource source;
	protected final T client;

	private SenderResult senderResult;

	protected AbstractSender(DataSource source, T client) {
		this.source = source;
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
