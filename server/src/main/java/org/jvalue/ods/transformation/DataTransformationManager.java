/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.transformation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import javax.script.ScriptException;
import java.io.IOException;

public class DataTransformationManager {
	private final ExecutionEngine executionEngine;


	@Inject
	public DataTransformationManager(ExecutionEngine executionEngine) {
		this.executionEngine = executionEngine;
	}


	public ObjectNode transform(ObjectNode data, TransformationFunction transformationFunction)
		throws ScriptException, IOException, NoSuchMethodException {
		return executionEngine.execute(data, transformationFunction);
	}
}
