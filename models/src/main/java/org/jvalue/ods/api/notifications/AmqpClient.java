/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AmqpClient extends Client {

    public static final String CLIENT_TYPE = "AMQP";

    @NotNull private final String exchange;
    @NotNull private final String uri;
    @NotNull private final String exchangeType;
    private final String routingKey;

    public AmqpClient(
            @JsonProperty("id") String id,
            @JsonProperty("uri") String uri,
            @JsonProperty("exchange") String exchange,
			@JsonProperty("exchangeType") String exchangeType,
			@JsonProperty("routingKey") String routingKey) {

        super(id, CLIENT_TYPE);
        this.exchange = exchange;
        this.uri = uri;
        this.exchangeType = exchangeType;
		if (routingKey == null) {
			this.routingKey = "";
		} else  {
			this.routingKey = routingKey;
		}
    }


    public String getUri() {
        return uri;
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
		AmqpClient that = (AmqpClient) o;
		return Objects.equals(exchange, that.exchange) &&
			Objects.equals(uri, that.uri) &&
			Objects.equals(exchangeType, that.exchangeType) &&
			Objects.equals(routingKey, that.routingKey);
	}


	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), exchange, uri, exchangeType, routingKey);
	}


	@Override
    public <P, R> R accept(ClientVisitor<P, R> visitor, P param) {
        return visitor.visit(this, param);
    }
}
