package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class NdsClientDescription extends ClientDescription {

	public static final String CLIENT_TYPE = "NDS";

	@NotNull private final String host;
	@NotNull private final String exchange;

	@JsonCreator
	public NdsClientDescription(
		@JsonProperty("host") String host,
		@JsonProperty("exchange") String exchange) {

		super(CLIENT_TYPE);
		this.host = host;
		this.exchange = exchange;
	}


	public String getHost() {
		return host;
	}


	public String getExchange() {
		return exchange;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		NdsClientDescription that = (NdsClientDescription) o;
		return Objects.equals(host, that.host) &&
			Objects.equals(exchange, that.exchange);
	}


	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), host, exchange);
	}


	@Override
	public <P, R> R accept(ClientDescriptionVisitor<P, R> visitor, P param) {
		return visitor.visit(this, param);
	}
}
