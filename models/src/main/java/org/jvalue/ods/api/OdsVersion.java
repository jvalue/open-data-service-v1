package org.jvalue.ods.api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public final class OdsVersion {

	@NotNull private final String versionDescription;
	@NotNull private final int versionNumber;
	@NotNull private final String build;

	@JsonCreator
	public OdsVersion(
			@JsonProperty("versionDescription") String versionDescription,
			@JsonProperty("versionNumber") int versionNumber,
			@JsonProperty("build") String build) {

		this.versionDescription = versionDescription;
		this.versionNumber = versionNumber;
		this.build = build;
	}


	public String getVersionDescription() {
		return versionDescription;
	}


	public int getVersionNumber() {
		return versionNumber;
	}


	public String getBuild() {
		return build;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof OdsVersion)) return false;
		OdsVersion version = (OdsVersion) other;
		return Objects.equal(versionDescription, version.versionDescription)
				&& Objects.equal(versionNumber, version.versionNumber)
				&& Objects.equal(build, version.build);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(versionDescription, versionNumber, build);
	}

}
