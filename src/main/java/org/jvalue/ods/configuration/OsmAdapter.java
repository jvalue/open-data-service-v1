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
package org.jvalue.ods.configuration;

import org.jvalue.ods.filter.Filter;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.RelationMember;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.RunnableSource;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.xml.common.CompressionMethod;
import org.openstreetmap.osmosis.xml.v0_6.XmlReader;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

final class OsmAdapter extends Filter<File, Object> {

	@Override
	protected Object doProcess(File file) {
		final List<Object> resultList = new LinkedList<Object>();

		Sink sinkImplementation = new Sink() {
			public void process(EntityContainer entityContainer) {
				Entity entity = entityContainer.getEntity();

				if (entity instanceof Node) {
					resultList.add(convertNodeToGenericValue((Node) entity));
				} else if (entity instanceof Way) {
					resultList.add(convertWayToGenericValue((Way) entity));
				} else if (entity instanceof Relation) {
					resultList.add(convertRelationToGenericValue((Relation) entity));
				}
				// else if (entity instanceof Bound) {
				//
				// }
			}

			public void release() { }

			public void complete() { }

			public void initialize(Map<String, Object> metaData) { }
		};

		RunnableSource reader = new XmlReader(file, false,
				CompressionMethod.None);

		reader.setSink(sinkImplementation);

		Thread readerThread = new Thread(reader);
		readerThread.start();

		while (readerThread.isAlive()) {
			try {
				readerThread.join();
			} catch (InterruptedException e) {
				// maybe throw another exception here instead of returning null
				return null;
			}
		}

		return resultList;
	}


	@Override
	protected void doOnComplete() {
		// nothing to do here
	}

	private Map<String, Object> convertRelationToGenericValue(Relation relation) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "Relation");

		map.put("relationId", "" + relation.getId());
		map.put("timestamp", relation.getTimestamp().toString());
		map.put("uid", relation.getUser().getId());
		map.put("user", relation.getUser().getName());
		map.put("version", relation.getVersion());
		map.put("changeset", relation.getChangesetId());

		Map<String, Object> tagsMap = new HashMap<String, Object>();
		Collection<Tag> coll = relation.getTags();
		for (Tag tag : coll) {
			tagsMap.put(tag.getKey(), tag.getValue());
		}

		List<Object> memberList = new LinkedList<Object>();
		for (RelationMember rm : relation.getMembers()) {
			Map<String, Object> membersMap = new HashMap<String, Object>();

			membersMap.put("type", rm.getMemberType().toString());
			membersMap.put("ref", rm.getMemberId());
			membersMap.put("role", rm.getMemberRole());
			memberList.add(membersMap);
		}

		map.put("members", memberList);

		return map;
	}


	private Map<String, Object> convertWayToGenericValue(Way w) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "Way");

		map.put("wayId", "" + w.getId());
		map.put("timestamp", w.getTimestamp().toString());
		map.put("uid", w.getUser().getId());
		map.put("user", w.getUser().getName());
		map.put("changeset", w.getChangesetId());
		map.put("version", w.getVersion());
		Map<String, Object> tagsMap = new HashMap<String, Object>();
		Collection<Tag> coll = w.getTags();
		for (Tag tag : coll) {
			tagsMap.put(tag.getKey(), tag.getValue());
		}
		map.put("tags", tagsMap);

		List<Object> list = new LinkedList<Object>();
		for (WayNode wn : w.getWayNodes()) {
			list.add(wn.getNodeId());
		}
		map.put("nd", list);

		return map;
	}


	private Map<String, Object> convertNodeToGenericValue(Node n) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "Node");

		map.put("nodeId", "" + n.getId());
		map.put("timestamp", n.getTimestamp().toString());
		map.put("uid", n.getUser().getId());
		map.put("user", n.getUser().getName());
		map.put("changeset", n.getChangesetId());
		map.put("latitude", n.getLatitude());
		map.put("longitude", n.getLongitude());

		Map<String, Object> tagsMap = new HashMap<String, Object>();
		Collection<Tag> coll = n.getTags();
		for (Tag tag : coll) {
			tagsMap.put(tag.getKey(), tag.getValue());
		}
		map.put("tags", tagsMap);

		return map;
	}
}
