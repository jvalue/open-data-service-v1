package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.time.LocalDateTime;

@RunWith(JMockit.class)
public final class AddTimestampFilterTest {

    @Mocked
    private MetricRegistry registry;

    private DataSource source;
    private ObjectNode baseNode;

    @Before
    public void setup() {
        this.source = createDataSource("/parent/id");
        this.baseNode = new ObjectNode(JsonNodeFactory.instance);
    }


    @Test
    public void testAddTimestamp() throws Exception {
        ObjectNode resultNode = applyFilter();

        String resultString = resultNode.get(AddTimestampFilter.DEFAULT_KEY_NAME).textValue();
        LocalDateTime resultDateTime = LocalDateTime.parse(resultString);

        Assert.assertFalse(resultString.isEmpty());
        Assert.assertEquals(resultString, resultDateTime.toString());
    }


    @Test
    public void testAddTimestamp_OverrideValue() throws Exception {
        String firstTimestamp = "2000-02-02T12:00:00";
        baseNode.put(AddTimestampFilter.DEFAULT_KEY_NAME, firstTimestamp);

        ObjectNode resultNode = applyFilter();
        String resultTimestamp = resultNode.get(AddTimestampFilter.DEFAULT_KEY_NAME).textValue();

        Assert.assertFalse(resultTimestamp.isEmpty());
        Assert.assertNotEquals(firstTimestamp, resultTimestamp);
    }


    private ObjectNode applyFilter() throws Exception {
        return new AddTimestampFilter(source, registry).doFilter(baseNode);
    }


    private DataSource createDataSource(String jsonPointer) {
        return new DataSource(
                "someId",
                JsonPointer.compile(jsonPointer),
                new ObjectNode(JsonNodeFactory.instance),
                new DataSourceMetaData("", "", "", "", "", "", ""));
    }
}
