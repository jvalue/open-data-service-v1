package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AmqpClientDescription extends ClientDescription {

    public static final String CLIENT_TYPE = "AMQP";

    @NotNull private final String exchange;
    @NotNull private final String host;
	@NotNull private final String exchangeType;
	private final String routingKey;

    @JsonCreator
    public AmqpClientDescription(
            @JsonProperty("host") String host,
            @JsonProperty("exchange") String exchange,
			@JsonProperty("exchangeType") String exchangeType,
			@JsonProperty("routingKey") String routingKey) {

        super(CLIENT_TYPE);
        this.exchange = exchange;
        this.host = host;
        this.exchangeType = exchangeType;
        this.routingKey = routingKey;
    }


    public String getHost() {
        return host;
    }


    public String getExchange() {
        return exchange;
    }


	public String getExchangeType() {
		return exchangeType;
	}


	public String getRoutingKey() {
		return routingKey;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		AmqpClientDescription that = (AmqpClientDescription) o;
		return Objects.equals(exchange, that.exchange) &&
			Objects.equals(host, that.host) &&
			Objects.equals(exchangeType, that.exchangeType) &&
			Objects.equals(routingKey, that.routingKey);
	}


	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), exchange, host, exchangeType, routingKey);
	}


	@Override
    public <P, R> R accept(ClientDescriptionVisitor<P, R> visitor, P param) {
        return visitor.visit(this, param);
    }
}
