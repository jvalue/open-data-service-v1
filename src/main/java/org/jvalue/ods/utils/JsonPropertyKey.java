package org.jvalue.ods.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import java.util.LinkedList;
import java.util.List;

public final class JsonPropertyKey {

	private final List<Entry> entries;

	private JsonPropertyKey(List<Entry> entries) {
		this.entries = entries;
	}


	public JsonNode getProperty(JsonNode node) {
		for (Entry entry : entries) {
			if (entry.isString()) node = node.path(entry.getString());
			else node = node.get(entry.getInt());
		}
		if (node.isMissingNode()) return null;
		else return node;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof JsonPropertyKey)) return false;
		if (other == this) return true;
		JsonPropertyKey key = (JsonPropertyKey) other;
		return Objects.equal(entries, key.entries);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(entries);
	}


	public static class Builder {
		private final List<Entry> entries = new LinkedList<>();

		public Builder stringPath(String stringPath) {
			entries.add(new Entry(stringPath));
			return this;
		}


		public Builder intPath(int intPath) {
			entries.add(new Entry(intPath));
			return this;
		}


		public JsonPropertyKey build() {
			return new JsonPropertyKey(entries);
		}
	}


	private static final class Entry {
		private final String stringKey;
		private final int intKey;

		public Entry(String stringKey) {
			this.stringKey = stringKey;
			this.intKey = -1;
		}

		public Entry(int intKey) {
			this.intKey = intKey;
			this.stringKey = null;
		}


		public String getString() {
			return stringKey;
		}


		public int getInt() {
			return intKey;
		}


		public boolean isString() {
			return (stringKey != null);
		}


		public boolean isInt() {
			return !isString();
		}


		@Override
		public boolean equals(Object other) {
			if (other == null || !(other instanceof Entry)) return false;
			if (other == this) return true;
			Entry entry = (Entry) other;
			return Objects.equal(stringKey, entry.stringKey)
					&& intKey == entry.intKey;
		}


		@Override
		public int hashCode() {
			return Objects.hashCode(stringKey, intKey);
		}

	}
}
