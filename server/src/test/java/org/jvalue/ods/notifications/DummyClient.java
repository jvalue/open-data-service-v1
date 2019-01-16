/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jvalue.ods.api.notifications.Client;
import org.jvalue.ods.api.notifications.ClientVisitor;


public final class DummyClient extends Client {


	@JsonCreator
	public DummyClient(
			@JsonProperty("clientId") String clientId, 
			@JsonProperty("source") String source) {

		super(clientId, source);
	}


	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		throw new UnsupportedOperationException("dummy");
	}


}
