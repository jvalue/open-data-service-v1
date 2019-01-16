/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

    SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.notifications.sender;

import org.jvalue.commons.utils.Assert;
import org.jvalue.ods.api.notifications.Client;



public final class SenderResult {

	public enum Status {
		SUCCESS,
		UPDATE_CLIENT,
		REMOVE_CLIENT,
		ERROR;
	}


	private final Status status;
	private final Client oldClient;
	private final Client newClient;
	private final Throwable errorCause;
	private final String errorMsg;
	

	private SenderResult(
			Status status,
			Client oldClient,
			Client newClient,
			Throwable errorCause,
			String errorMsg) {

		this.status = status;
		this.oldClient = oldClient;
		this.newClient = newClient;
		this.errorCause = errorCause;
		this.errorMsg = errorMsg;
	}


	public Status getStatus() {
		return status;
	}


	public Client getOldClient() {
		return oldClient;
	}


	public Client getNewClient() {
		return newClient;
	}
	

	public Throwable getErrorCause() {
		return errorCause;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	static class Builder {
		private final Status status;
		private Client oldClient, newClient;
		private Throwable errorCause;
		private String errorMsg;


		public Builder(Status status) {
			Assert.assertNotNull(status);
			this.status = status;
		}


		public Builder oldClient(Client oldClient) {
			this.oldClient = oldClient;
			return this;
		}


		public Builder newClient(Client newClient) {
			this.newClient = newClient;
			return this;
		}


		public Builder errorCause(Throwable errorCause) {
			this.errorCause = errorCause;
			return this;
		}


		public Builder errorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
			return this;
		}


		public SenderResult build() {
			return new SenderResult(status, oldClient, newClient, errorCause, errorMsg);
		}
	}

}
