package org.jvalue.ods.processor.adapter.domain.weather.dwd;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

/**
 * Interface for current and forecast response parsing strategies.
 */
interface NodeParsingStrategy {
	Iterator<ObjectNode> parseServiceResponse(Iterator<ObjectNode> nodeIterator);
}
