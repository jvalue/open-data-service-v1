/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class NdsClientDescription extends ClientDescription {

	public static final String CLIENT_TYPE = "NDS";

	@NotNull private final String uri;
	@NotNull private final String exchange;
	private final boolean validateMessage;

	@JsonCreator
	public NdsClientDescription(
		@JsonProperty("uri") String uri,
		@JsonProperty("exchange") String exchange,
		@JsonProperty("validateMessage") boolean validateMessage) {

		super(CLIENT_TYPE);
		this.uri = uri;
		this.exchange = exchange;
		this.validateMessage = validateMessage;
	}


	public String getUri() {
		return uri;
	}


	public String getExchange() {
		return exchange;
	}


	public boolean getValidateMessage() {
		return validateMessage;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		NdsClientDescription that = (NdsClientDescription) o;
		return Objects.equals(uri, that.uri) &&
			Objects.equals(exchange, that.exchange) &&
			Objects.equals(validateMessage, that.validateMessage);
	}


	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), uri, exchange, validateMessage);
	}


	@Override
	public <P, R> R accept(ClientDescriptionVisitor<P, R> visitor, P param) {
		return visitor.visit(this, param);
	}
}
