package org.jvalue.ods.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Assert;
import org.junit.Test;

public class ClientDescriptionTest {

    @Test
    public final void testEquals() {

        DummyClientDescription clientDesc1 = new DummyClientDescription("type_1");
        DummyClientDescription clientDesc2 = new DummyClientDescription("type_1");
        DummyClientDescription clientDesc3 = new DummyClientDescription("type_other");

        Assert.assertEquals(clientDesc1, clientDesc1);
        Assert.assertEquals(clientDesc1, clientDesc2);
        Assert.assertNotEquals(clientDesc1, clientDesc3);
        Assert.assertNotEquals(clientDesc1, null);
        Assert.assertNotEquals(clientDesc1, new Object());

        Assert.assertEquals(clientDesc1.hashCode(), clientDesc1.hashCode());
        Assert.assertEquals(clientDesc1.hashCode(), clientDesc2.hashCode());
        Assert.assertNotEquals(clientDesc1.hashCode(), clientDesc3.hashCode());

    }


    @Test
    public final void testGetType() {

        DummyClientDescription clientDesc = new DummyClientDescription("type_0x00");
        Assert.assertEquals(clientDesc.getType(), "type_0x00");

    }


    private static final class DummyClientDescription extends ClientDescription {

        @JsonCreator
        public DummyClientDescription(
                @JsonProperty("type") String type) {

            super(type);
        }


        @Override
        public <P, R> R accept(ClientDescriptionVisitor<P, R> visitor, P param) {
            throw new UnsupportedOperationException("dummy");
        }

    }

}
