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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class OdsNode.
 */
@JsonInclude(Include.NON_NULL)
public class OdsNode implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private String id;

	/** The revision. */
	private String revision;

	/** The node id. */
	private String nodeId;

	/** The timestamp. */
	private String timestamp;

	/** The uid. */
	private String uid;

	/** The user. */
	private String user;

	/** The version. */
	private String version;

	/** The changeset. */
	private String changeset;

	/** The latitude. */
	private String latitude;

	/** The longitude. */
	private String longitude;

	/** The tags. */
	private Map<String, String> tags;

	/**
	 * Instantiates a new ods node.
	 * 
	 * @param node
	 *            the node
	 */
	public OdsNode(Node node) {
		this.nodeId = "" + node.getId();
		this.timestamp = node.getTimestamp().toString();
		this.uid = "" + node.getUser().getId();
		this.user = node.getUser().getName();
		this.version = "" + node.getVersion();
		this.changeset = "" + node.getChangesetId();
		this.latitude = "" + node.getLatitude();
		this.longitude = "" + node.getLongitude();
		this.tags = new HashMap<String, String>();
		for (Tag t : node.getTags()) {
			tags.put(t.getKey(), t.getValue());
		}
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@JsonProperty("_id")
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param s
	 *            the new id
	 */
	@JsonProperty("_id")
	public void setId(String s) {
		id = s;
	}

	/**
	 * Gets the revision.
	 * 
	 * @return the revision
	 */
	@JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}

	/**
	 * Sets the revision.
	 * 
	 * @param s
	 *            the new revision
	 */
	@JsonProperty("_rev")
	public void setRevision(String s) {
		revision = s;
	}

	/**
	 * Gets the node id.
	 * 
	 * @return the node id
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the node id.
	 * 
	 * @param nodeId
	 *            the new node id
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 * 
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the uid.
	 * 
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Sets the uid.
	 * 
	 * @param uid
	 *            the new uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the changeset.
	 * 
	 * @return the changeset
	 */
	public String getChangeset() {
		return changeset;
	}

	/**
	 * Sets the changeset.
	 * 
	 * @param changeset
	 *            the new changeset
	 */
	public void setChangeset(String changeset) {
		this.changeset = changeset;
	}

	/**
	 * Gets the latitude.
	 * 
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 * 
	 * @param latitude
	 *            the new latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 * 
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 * 
	 * @param longitude
	 *            the new longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the tags.
	 * 
	 * @return the tags
	 */
	public Map<String, String> getTags() {
		return tags;
	}

	/**
	 * Sets the tags.
	 * 
	 * @param tags
	 *            the tags
	 */
	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	/**
	 * Gets the serialversionuid.
	 * 
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
