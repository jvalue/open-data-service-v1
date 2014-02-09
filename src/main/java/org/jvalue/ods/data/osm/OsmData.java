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
package org.jvalue.ods.data.osm;

import java.util.LinkedList;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;

/**
 * The Class OsmData.
 */
public class OsmData {

	/** The nodes. */
	private List<OdsNode> nodes;

	/** The ways. */
	private List<OdsWay> ways;

	/** The relations. */
	private List<OdsRelation> relations;

	/**
	 * Instantiates a new osm data.
	 * 
	 * @param nodes
	 *            the nodes
	 * @param ways
	 *            the ways
	 * @param relations
	 *            the relations
	 */
	public OsmData(List<Node> nodes, List<Way> ways, List<Relation> relations) {

		this.nodes = new LinkedList<OdsNode>();
		for (Node n : nodes) {
			this.nodes.add(new OdsNode(n));
		}

		this.ways = new LinkedList<OdsWay>();
		for (Way w : ways) {
			this.ways.add(new OdsWay(w));
		}

		this.relations = new LinkedList<OdsRelation>();
		for (Relation r : relations) {
			this.relations.add(new OdsRelation(r));
		}
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<OdsNode> getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<OdsNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the ways.
	 * 
	 * @return the ways
	 */
	public List<OdsWay> getWays() {
		return ways;
	}

	/**
	 * Sets the ways.
	 * 
	 * @param ways
	 *            the new ways
	 */
	public void setWays(List<OdsWay> ways) {
		this.ways = ways;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return the relations
	 */
	public List<OdsRelation> getRelations() {
		return relations;
	}

	/**
	 * Sets the relations.
	 * 
	 * @param relations
	 *            the new relations
	 */
	public void setRelations(List<OdsRelation> relations) {
		this.relations = relations;
	}

}
