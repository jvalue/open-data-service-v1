package org.jvalue.common.utils;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public final class VersionInfo {

	@NotNull private final String version;
	@NotNull private final String build;

	@JsonCreator
	public VersionInfo(
			@JsonProperty("version") String version,
			@JsonProperty("build") String build) {

		this.version = version;
		this.build = build;
	}


	public String getVersion() {
		return version;
	}


	public String getBuild() {
		return build;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof VersionInfo)) return false;
		VersionInfo info = (VersionInfo) other;
		return Objects.equal(version, info.version)
				&& Objects.equal(build, info.build);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(version, build);
	}

}
