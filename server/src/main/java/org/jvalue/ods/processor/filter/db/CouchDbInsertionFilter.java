/*  Open Data Service
    Copyright (C) 2013  Tsysin Konstantin, Reischl Patrick

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package org.jvalue.ods.processor.filter.db;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.ektorp.DocumentOperationResult;
import org.jvalue.ods.admin.monitoring.PauseableTimer;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.views.couchdb.CouchDbDataView;
import org.jvalue.commons.db.repositories.GenericDataRepository;
import org.jvalue.ods.processor.filter.AbstractFilter;
import org.jvalue.ods.processor.filter.FilterException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CouchDbInsertionFilter extends AbstractFilter<ObjectNode, ObjectNode> {

	protected final GenericDataRepository<CouchDbDataView, JsonNode> dataRepository;
	private final boolean updateDataIfExists;

	// for bulk operations
	private static final int BULK_SIZE = 2000;
	final List<String> bulkDomainIds = new LinkedList<>();
	final List<ObjectNode> bulkObjects = new LinkedList<>();

	PauseableTimer.Context timerContextBulkRead, timerContextBulkWrite;



	@Inject
	CouchDbInsertionFilter(
			@Assisted GenericDataRepository<CouchDbDataView, JsonNode> dataRepository,
			@Assisted DataSource source,
			@Assisted boolean updateDataIfExists,
			MetricRegistry registry) {

		super(source, registry);
		this.dataRepository = dataRepository;
		this.updateDataIfExists = updateDataIfExists;

		PauseableTimer timerBulkRead = PauseableTimer.createTimer(registry, MetricRegistry.name(CouchDbInsertionFilter.class, "bulk-read"));
		PauseableTimer timerBulkWrite = PauseableTimer.createTimer(registry, MetricRegistry.name(CouchDbInsertionFilter.class, "bulk-write"));
		this.timerContextBulkRead = timerBulkRead.createContext();
		this.timerContextBulkWrite = timerBulkWrite.createContext();
	}


	@Override
	protected ObjectNode doFilter(ObjectNode node) throws FilterException {
		String domainKey = node.at(source.getDomainIdKey()).asText();
		bulkDomainIds.add(domainKey);
		bulkObjects.add(node);
		if (bulkObjects.size() >= BULK_SIZE) writeBulkData();
		return node;
	}


	@Override
	protected void doOnComplete() throws FilterException {
		writeBulkData();
		dataRepository.compact();
		if (updateDataIfExists) timerContextBulkRead.stop();
		timerContextBulkWrite.stop();
	}


	@SuppressWarnings("unchecked")
	protected void writeBulkData() throws FilterException {
		if (updateDataIfExists) {
			timerContextBulkRead.resume();
			Map<String, JsonNode> bulkLoaded = dataRepository.getBulk(bulkDomainIds);
			try {
				for (ObjectNode node : bulkObjects) {
					String domainId = node.at(source.getDomainIdKey()).asText();
					if (bulkLoaded.containsKey(domainId)) {
						JsonNode oldNode = bulkLoaded.get(domainId);
						node.put("_id", oldNode.get("_id").asText());
						node.put("_rev", oldNode.get("_rev").asText());
					}
				}
			} finally {
				timerContextBulkRead.pause();
			}
		}

		timerContextBulkWrite.resume();
		try {
			Collection<DocumentOperationResult> results = dataRepository.writeBulk((List) bulkObjects);
			for (DocumentOperationResult result : results) {
				if (result.isErroneous())
					throw new FilterException("db insertion failed for id " + result.getId() + ", reason: " + result.getReason());
			}
		} finally {
			timerContextBulkWrite.pause();
		}

		bulkObjects.clear();
	}

}
