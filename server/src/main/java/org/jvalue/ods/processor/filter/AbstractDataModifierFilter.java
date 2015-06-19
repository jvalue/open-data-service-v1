package org.jvalue.ods.processor.filter;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.assistedinject.Assisted;

import org.jvalue.ods.api.sources.DataSource;


/**
 * Contains a various number of helper methods for modifying data (e.g. rename, delete, add ...).
 */
public abstract class AbstractDataModifierFilter extends AbstractFilter<ObjectNode, ObjectNode> {

	protected AbstractDataModifierFilter(@Assisted DataSource source, MetricRegistry registry) {
		super(source, registry);
	}


	protected void rename(ObjectNode node, String oldField, String newField) {
		JsonNode nodeToRename = node.get(oldField);
		node.remove(oldField);
		node.put(newField, nodeToRename);
	}

}
