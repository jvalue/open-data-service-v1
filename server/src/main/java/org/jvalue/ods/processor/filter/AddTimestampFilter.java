package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;
import org.jvalue.ods.api.sources.DataSource;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class AddTimestampFilter extends AbstractFilter<ObjectNode, ObjectNode> {

    public static final String DEFAULT_KEY_NAME = "created_at";

    @Inject
    AddTimestampFilter(
            @Assisted DataSource source,
            MetricRegistry registry) {
        super(source, registry);
    }


    @Override
    protected ObjectNode doFilter(ObjectNode node) throws FilterException {
        JsonNode keyNode = node.at(source.getDomainIdKey());


        node.put(DEFAULT_KEY_NAME, LocalDateTime.now().toString());
        return node;
    }


    @Override
    protected void doOnComplete() throws FilterException {

    }


}
