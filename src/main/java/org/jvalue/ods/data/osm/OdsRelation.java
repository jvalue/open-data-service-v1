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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.RelationMember;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class OdsRelation.
 */
@JsonInclude(Include.NON_NULL)
public class OdsRelation implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private String id;

	/** The revision. */
	private String revision;

	/** The relation id. */
	private String relationId;

	/** The timestamp. */
	private String timestamp;

	/** The uid. */
	private String uid;

	/** The user. */
	private String user;

	/** The visible. */
	private String visible;

	/** The version. */
	private String version;

	/** The changeset. */
	private String changeset;

	/** The tags. */
	private Map<String, String> tags;

	/** The members. */
	private List<HashMap<String, String>> members;

	/**
	 * Instantiates a new ods relation.
	 * 
	 * @param relation
	 *            the relation
	 */
	public OdsRelation(Relation relation) {

		this.relationId = "" + relation.getId();
		this.timestamp = relation.getTimestamp().toString();
		this.uid = "" + relation.getUser().getId();
		this.user = relation.getUser().getName();
		this.visible = "true";
		this.version = "" + relation.getVersion();
		this.changeset = "" + relation.getChangesetId();

		this.tags = new HashMap<String, String>();

		for (Tag t : relation.getTags()) {
			tags.put(t.getKey(), t.getValue());
		}

		this.members = new LinkedList<HashMap<String, String>>();

		for (RelationMember rm : relation.getMembers()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("type", rm.getMemberType().toString());
			map.put("ref", "" + rm.getMemberId());
			map.put("role", rm.getMemberRole());
			members.add(map);
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
	 * Gets the relation id.
	 * 
	 * @return the relation id
	 */
	public String getRelationId() {
		return relationId;
	}

	/**
	 * Sets the relation id.
	 * 
	 * @param relationId
	 *            the new relation id
	 */
	public void setRelationId(String relationId) {
		this.relationId = relationId;
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
	 * Gets the visible.
	 * 
	 * @return the visible
	 */
	public String getVisible() {
		return visible;
	}

	/**
	 * Sets the visible.
	 * 
	 * @param visible
	 *            the new visible
	 */
	public void setVisible(String visible) {
		this.visible = visible;
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
	 * Gets the members.
	 * 
	 * @return the members
	 */
	public List<HashMap<String, String>> getMembers() {
		return members;
	}

	/**
	 * Sets the members.
	 * 
	 * @param members
	 *            the members
	 */
	public void setMembers(List<HashMap<String, String>> members) {
		this.members = members;
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
