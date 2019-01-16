/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jvalue.ods.api.notifications.Client;


public interface Sender<T extends Client> {

	public void onNewDataStart();
	public void onNewDataItem(ObjectNode data);
	public void onNewDataComplete();
	public SenderResult getSenderResult();

}
