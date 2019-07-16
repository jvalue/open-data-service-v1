/*
 * Copyright (c) 2019 Friedrich-Alexander University Erlangen-Nuernberg (FAU)
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */
package org.jvalue.ods.admin.rest;


import com.google.inject.Inject;
import org.jvalue.commons.utils.ListValueMap;
import org.jvalue.ods.api.processors.ProcessorReferenceChain;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.processor.ProcessorChainManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/filterChains")
@Produces(MediaType.APPLICATION_JSON)
public final class AdminFilterChainApi {

	private final ProcessorChainManager chainManager;

	@Inject
	public AdminFilterChainApi(
			ProcessorChainManager chainManager) {

		this.chainManager = chainManager;
	}


	@GET
	public Map<String, List<ProcessorReferenceChain>> getAllRunningTasks() {
		ListValueMap<DataSource, ProcessorReferenceChain> tasks = chainManager.getAllRunningTasks();
		Map<String, List<ProcessorReferenceChain>> result = new HashMap<>();
		for (Map.Entry<DataSource, List<ProcessorReferenceChain>> entry : tasks.getAll().entrySet()) {
			result.put(entry.getKey().getId(), entry.getValue());
		}
		return result;
	}

}
