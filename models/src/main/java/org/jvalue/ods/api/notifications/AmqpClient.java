package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AmqpClient extends Client {

    static final String CLIENT_TYPE = "AMQP";

    @NotNull private final String exchange;
    @NotNull private final String host;

    public AmqpClient(
            @JsonProperty("id") String id,
            @JsonProperty("host") String host,
            @JsonProperty("exchange") String exchange) {

        super(id, CLIENT_TYPE);
        this.exchange = exchange;
        this.host = host;
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
        AmqpClient that = (AmqpClient) o;
        return Objects.equals(exchange, that.exchange) &&
                Objects.equals(host, that.host);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), exchange, host);
    }


    @Override
    public <P, R> R accept(ClientVisitor<P, R> visitor, P param) {
        return visitor.visit(this, param);
    }
}
