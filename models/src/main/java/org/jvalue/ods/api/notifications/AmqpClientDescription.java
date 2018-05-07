package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class AmqpClientDescription extends ClientDescription {

    static final String CLIENT_TYPE = "AMQP";

    @NotNull private final String exchange;
    @NotNull private final String host;

    @JsonCreator
    public AmqpClientDescription(
            @JsonProperty("host") String host,
            @JsonProperty("exchange") String exchange) {

        super(CLIENT_TYPE);
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
        AmqpClientDescription that = (AmqpClientDescription) o;
        return Objects.equals(exchange, that.exchange) &&
                Objects.equals(host, that.host);
    }


    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), exchange, host);
    }


    @Override
    public <P, R> R accept(ClientDescriptionVisitor<P, R> visitor, P param) {
        return visitor.visit(this, param);
    }
}
